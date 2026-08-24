package com.example.noticesorter.domain

import android.content.Context
import android.net.Uri
import com.example.noticesorter.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.serialization.json.Json

class NoticeProcessor {
    private val ocrEngine = OcrEngine()
    
    // We will use gemini-1.5-flash for speed and cost effectiveness since it's a simple extraction
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
        val extractedText = ocrEngine.extractText(context, imageUri)
        if (extractedText.isBlank()) {
            return NoticeData(
                title = "Unknown Notice",
                date = "",
                type = "other",
                action_needed = "Could not extract text from the image.",
                confidence = "low"
            )
        }

        // 2. Pass to LLM
        val prompt = """
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

        return try {
            val response = generativeModel.generateContent(content { text(prompt) })
            var responseText = response.text ?: ""
            
            // Fallback parser: Strip markdown code blocks if the LLM includes them
            if (responseText.contains("```json")) {
                responseText = responseText.substringAfter("```json").substringBeforeLast("```")
            } else if (responseText.contains("```")) {
                responseText = responseText.substringAfter("```").substringBeforeLast("```")
            }
            
            jsonParser.decodeFromString<NoticeData>(responseText.trim())
        } catch (e: Exception) {
            e.printStackTrace()
            // Graceful degradation on parse failure
            NoticeData(
                title = "Parsing Error",
                date = "",
                type = "other",
                action_needed = "Failed to understand the notice details.",
                confidence = "low"
            )
        }
    }
}
