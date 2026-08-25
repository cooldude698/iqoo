package com.collegeos.feature.noticesorter.data

import android.content.Context
import android.net.Uri
import com.collegeos.feature.noticesorter.model.NoticeData
import kotlinx.coroutines.delay

import android.util.Log
import com.collegeos.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json

interface NoticeProcessor {
    suspend fun processNotice(imageUri: String, context: Context? = null): NoticeData
}

/**
 * Live OCR + LLM Notice Processor (Prit's Pipeline)
 */
class RealNoticeProcessor : NoticeProcessor {
    private val ocrEngine = OcrEngine()
    
    // Future: Replace cloud LLM with Gemini Nano on-device for fully offline, private operation
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun processNotice(imageUri: String, context: Context?): NoticeData {
        if (context == null) return MockNoticeProcessor().processNotice(imageUri, context)
        
        val uri = Uri.parse(imageUri)
        var extractedText = ocrEngine.extractText(context, uri)
        
        if (extractedText.trim().length < 10) {
            Log.w("NoticeSorter-OCR", "Text too short or empty, fast-failing.")
            return NoticeData(
                title = "Could not read this notice",
                date = null,
                type = "other",
                actionNeeded = "Image may be too blurry or contain no text. Please enter details manually.",
                confidence = "low"
            )
        }

        if (extractedText.length > 3000) {
            Log.d("NoticeSorter-OCR", "Text exceeds 3000 chars. Truncating.")
            extractedText = extractedText.take(3000) + "...[truncated]"
        }

        val prompt = """
            Today's date is ${java.time.LocalDate.now()}. If the notice contains relative dates
            (e.g., "next Monday", "this Friday"), convert them to absolute dates
            based on today. If no year is mentioned, assume the current academic year.

            If the notice is in Hindi or another Indian language, extract the information but return the title and action_needed in English.

            You will receive raw OCR text extracted from a photo or PDF of a notice
            shared in an Indian student WhatsApp group. Extract the following as JSON only,
            no extra text, no markdown code blocks:

            {
              "title": "short, clear title for this notice",
              "date": "YYYY-MM-DD, the most important/actionable date in the notice. If no year given, assume current year.",
              "time": "HH:MM in 24hr format, or null if not specified",
              "type": "exam | fee | event | circular | other",
              "action_needed": "one short sentence describing what the student needs to do",
              "confidence": "high if date is explicit and clear, low if you had to guess or no date was found"
            }

            OCR TEXT:
            $extractedText
        """.trimIndent()
        
        Log.d("NoticeSorter-LLM", "Prompt sent (${prompt.length} chars): ${prompt.take(200).replace("\n", " ")}...")

        return try {
            val responseText = fetchFromLlmWithRetry(prompt)
            Log.d("NoticeSorter-LLM", "Raw LLM response: $responseText")
            
            val jsonString = sanitizeJsonResponse(responseText)
            val noticeData = jsonParser.decodeFromString<NoticeData>(jsonString)
            validateNoticeData(noticeData)
        } catch (e: Exception) {
            Log.e("NoticeSorter-LLM", "Parsing Error or Timeout", e)
            NoticeData(
                title = "Parsing Error",
                date = null,
                type = "other",
                actionNeeded = "Failed to understand the notice details.",
                confidence = "low"
            )
        }
    }

    private suspend fun fetchFromLlmWithRetry(prompt: String): String {
        val maxRetries = 1
        var currentAttempt = 0
        
        while (true) {
            try {
                return withTimeout(15000) {
                    val response = generativeModel.generateContent(com.google.ai.client.generativeai.type.content { text(prompt) })
                    response.text ?: ""
                }
            } catch (e: Exception) {
                currentAttempt++
                if (currentAttempt > maxRetries) {
                    throw e
                }
                Log.w("NoticeSorter-LLM", "LLM call failed, retrying in 2 seconds...", e)
                delay(2000)
            }
        }
    }

    private fun sanitizeJsonResponse(response: String): String {
        var sanitized = response.trim()
        val startIndex = sanitized.indexOf('{')
        val endIndex = sanitized.lastIndexOf('}')
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            sanitized = sanitized.substring(startIndex, endIndex + 1)
        }
        return sanitized
    }

    private fun validateNoticeData(data: NoticeData): NoticeData {
        var confidence = data.confidence
        
        if (data.title.isBlank() || data.type.isBlank()) {
            confidence = "low"
        }
        
        if (data.date != null) {
            val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
            if (!dateRegex.matches(data.date)) {
                confidence = "low"
            }
        } else {
            confidence = "low"
        }

        val result = data.copy(confidence = confidence)
        Log.d("NoticeSorter-Result", "Final NoticeData: $result")
        return result
    }
}

/**
 * Mock Notice Processor for instant UI development & testing.
 */
class MockNoticeProcessor : NoticeProcessor {
    override suspend fun processNotice(imageUri: String, context: Context?): NoticeData {
        delay(1200)

        return when {
            imageUri.contains("fee", ignoreCase = true) -> NoticeData(
                title = "Even Semester Tuition Fee Payment",
                date = "2026-09-05",
                time = "17:00",
                type = "fee",
                actionNeeded = "Pay semester tuition fee of Rs 45,000 on student portal before 5 PM to avoid fine.",
                confidence = "high"
            )
            imageUri.contains("event", ignoreCase = true) -> NoticeData(
                title = "Tech Fest Hackathon Registration",
                date = "2026-08-30",
                time = "10:00",
                type = "event",
                actionNeeded = "Register team of 3 on college portal for 24-hour hackathon track.",
                confidence = "high"
            )
            imageUri.contains("low", ignoreCase = true) -> NoticeData(
                title = "Lab Exam Reschedule Notice",
                date = "",
                time = null,
                type = "exam",
                actionNeeded = "Check department notice board for revised slot allocation.",
                confidence = "low"
            )
            else -> NoticeData(
                title = "Mid-Term Examination Schedule - CS & EC",
                date = "2026-09-12",
                time = "09:30",
                type = "exam",
                actionNeeded = "Submit hall ticket form & bring valid college ID to Exam Hall 3.",
                confidence = "high"
            )
        }
    }
}
