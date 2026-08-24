package com.example.noticesorter.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.noticesorter.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import java.time.LocalDate

class NoticeProcessor {
    private val ocrEngine = OcrEngine()
    
    // Future: Replace cloud LLM with Gemini Nano on-device for fully offline, private operation — natively supported on iQOO devices with Snapdragon 8 Gen 3.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun processNotice(context: Context, imageUri: Uri): NoticeData {
        // 1. Extract text via OCR
        var extractedText = ocrEngine.extractText(context, imageUri)
        
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
            Today's date is ${LocalDate.now()}. If the notice contains relative dates
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
                    val response = generativeModel.generateContent(content { text(prompt) })
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
