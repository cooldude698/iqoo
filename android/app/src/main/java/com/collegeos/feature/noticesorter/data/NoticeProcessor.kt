package com.collegeos.feature.noticesorter.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.collegeos.BuildConfig
import com.collegeos.feature.noticesorter.model.NoticeData
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json

interface NoticeProcessor {
    suspend fun processNotice(imageUri: String, context: Context? = null): NoticeData
}

/**
 * Live OCR + LLM Notice Processor with Smart Regex Fallback
 */
class RealNoticeProcessor : NoticeProcessor {
    private val ocrEngine = OcrEngine()

    private val generativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun processNotice(imageUri: String, context: Context?): NoticeData {
        if (context == null) return MockNoticeProcessor().processNotice(imageUri, context)

        return try {
            val uri = Uri.parse(imageUri)
            var extractedText = ocrEngine.extractText(context, uri)

            if (extractedText.trim().length < 5) {
                Log.w("NoticeSorter-OCR", "OCR text empty or too short. Returning fallback.")
                return createFallbackNotice(extractedText, "Could not extract clear text. Please edit details.")
            }

            if (extractedText.length > 3000) {
                extractedText = extractedText.take(3000) + "...[truncated]"
            }

            val prompt = """
                Today's date is ${java.time.LocalDate.now()}. If the notice contains relative dates
                (e.g., "next Monday", "this Friday"), convert them to absolute dates in YYYY-MM-DD format.
                If no year is mentioned, assume 2026.

                If the notice is in Hindi or another language, translate title and action_needed to clear English.

                You will receive raw OCR text extracted from a photo or PDF of a notice shared in an Indian student group.
                Extract the following as JSON only, no extra text, no markdown code blocks:

                {
                  "title": "short, clear title for this notice",
                  "date": "YYYY-MM-DD, the primary deadline or event date",
                  "time": "HH:MM in 24hr format, or null if unspecified",
                  "type": "exam | fee | event | circular | other",
                  "action_needed": "one short sentence describing what the student must do",
                  "confidence": "high if date is explicit, low if guessed or missing"
                }

                OCR TEXT:
                $extractedText
            """.trimIndent()

            val responseText = fetchFromLlmWithRetry(prompt)
            val jsonString = sanitizeJsonResponse(responseText)
            val noticeData = jsonParser.decodeFromString<NoticeData>(jsonString)
            validateAndEnrichNoticeData(noticeData, extractedText)
        } catch (e: Exception) {
            Log.e("NoticeSorter-LLM", "LLM Extraction Error, using smart OCR fallback", e)
            val uri = Uri.parse(imageUri)
            val rawText = try { ocrEngine.extractText(context, uri) } catch (_: Exception) { "" }
            createFallbackNotice(rawText, "Notice extracted via on-device OCR. Tap fields to verify.")
        }
    }

    private suspend fun fetchFromLlmWithRetry(prompt: String): String {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            throw IllegalStateException("API key missing")
        }
        return withTimeout(12000) {
            val response = generativeModel.generateContent(com.google.ai.client.generativeai.type.content { text(prompt) })
            response.text ?: ""
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

    private fun validateAndEnrichNoticeData(data: NoticeData, rawText: String): NoticeData {
        var finalDate = data.date
        var confidence = data.confidence

        // If date is null or invalid, run smart regex extractor on raw OCR text
        if (finalDate.isNullOrBlank() || finalDate == "null" || !Regex("""^\d{4}-\d{2}-\d{2}$""").matches(finalDate)) {
            val extractedDate = extractDateFromText(rawText)
            if (extractedDate != null) {
                finalDate = extractedDate
                confidence = "high"
            } else {
                finalDate = "2026-09-05" // Fallback to realistic upcoming date so date is never blank!
                confidence = "low"
            }
        }

        val title = data.title.ifBlank {
            rawText.lines().firstOrNull { it.isNotBlank() }?.take(40) ?: "Campus Notice"
        }

        val action = data.actionNeeded.ifBlank {
            "Check department notice board or college portal for details."
        }

        return data.copy(
            title = title,
            date = finalDate,
            actionNeeded = action,
            confidence = confidence
        )
    }

    private fun createFallbackNotice(rawText: String, fallbackAction: String): NoticeData {
        val lines = rawText.lines().filter { it.isNotBlank() }
        val title = lines.firstOrNull()?.take(50) ?: "Campus Academic Notice"
        val detectedDate = extractDateFromText(rawText) ?: "2026-09-05"

        val noticeType = when {
            rawText.contains("hackathon", ignoreCase = true) || rawText.contains("fest", ignoreCase = true) || rawText.contains("event", ignoreCase = true) -> "event"
            rawText.contains("exam", ignoreCase = true) || rawText.contains("midterm", ignoreCase = true) || rawText.contains("test", ignoreCase = true) -> "exam"
            rawText.contains("fee", ignoreCase = true) || rawText.contains("tuition", ignoreCase = true) || rawText.contains("payment", ignoreCase = true) -> "fee"
            else -> "circular"
        }

        return NoticeData(
            title = title,
            date = detectedDate,
            time = "10:00",
            type = noticeType,
            actionNeeded = lines.drop(1).take(2).joinToString(" ").ifBlank { fallbackAction },
            confidence = if (extractDateFromText(rawText) != null) "high" else "low"
        )
    }

    /**
     * Smart Regex Date Extractor for Indian College Notices
     */
    private fun extractDateFromText(text: String): String? {
        if (text.isBlank()) return null

        // 1. Try YYYY-MM-DD
        val ymdRegex = Regex("""\b(202[4-9])[-/.](0[1-9]|1[0-2])[-/.](0[1-9]|[12][0-9]|3[01])\b""")
        ymdRegex.find(text)?.let {
            val (y, m, d) = it.destructured
            return "$y-$m-$d"
        }

        // 2. Try DD-MM-YYYY or DD/MM/YYYY
        val dmyRegex = Regex("""\b(0[1-9]|[12][0-9]|3[01])[-/.](0[1-9]|1[0-2])[-/.](202[4-9])\b""")
        dmyRegex.find(text)?.let {
            val (d, m, y) = it.destructured
            return "$y-$m-$d"
        }

        // 3. Try Month Name e.g. "30 August 2026", "30th Aug 2026", "August 30, 2026"
        val monthNames = mapOf(
            "jan" to "01", "feb" to "02", "mar" to "03", "apr" to "04", "may" to "05", "jun" to "06",
            "jul" to "07", "aug" to "08", "sep" to "09", "oct" to "10", "nov" to "11", "dec" to "12"
        )
        val namedMonthRegex = Regex("""\b(0?[1-9]|[12][0-9]|3[01])(?:st|nd|rd|th)?\s+(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*[\s,]+(202[4-9])?\b""", RegexOption.IGNORE_CASE)
        namedMonthRegex.find(text)?.let { match ->
            val dayStr = match.groupValues[1].padStart(2, '0')
            val monthStr = monthNames[match.groupValues[2].take(3).lowercase()] ?: "08"
            val yearStr = match.groupValues[3].ifEmpty { "2026" }
            return "$yearStr-$monthStr-$dayStr"
        }

        val reversedNamedMonthRegex = Regex("""\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\s+(0?[1-9]|[12][0-9]|3[01])(?:st|nd|rd|th)?[\s,]+(202[4-9])?\b""", RegexOption.IGNORE_CASE)
        reversedNamedMonthRegex.find(text)?.let { match ->
            val monthStr = monthNames[match.groupValues[1].take(3).lowercase()] ?: "08"
            val dayStr = match.groupValues[2].padStart(2, '0')
            val yearStr = match.groupValues[3].ifEmpty { "2026" }
            return "$yearStr-$monthStr-$dayStr"
        }

        return null
    }
}

/**
 * Mock Notice Processor for instant UI development & live hackathon testing.
 */
class MockNoticeProcessor : NoticeProcessor {
    override suspend fun processNotice(imageUri: String, context: Context?): NoticeData {
        delay(1200)

        return when {
            imageUri.contains("hackathon", ignoreCase = true) -> NoticeData(
                title = "iQOO OriginOS City Battle Hackathon 2026",
                date = "2026-08-30",
                time = "10:00",
                type = "event",
                actionNeeded = "Register team of 3 on portal & prepare 60-sec pitch for Smart Education Track.",
                confidence = "high"
            )
            imageUri.contains("robotics", ignoreCase = true) -> NoticeData(
                title = "National Autonomous Robotics Challenge",
                date = "2026-10-05",
                time = "10:30",
                type = "event",
                actionNeeded = "Submit circuit schematic and robot dimension blueprints to Lab 4.",
                confidence = "high"
            )
            imageUri.contains("fee", ignoreCase = true) -> NoticeData(
                title = "Even Semester Tuition Fee Payment Notice",
                date = "2026-09-05",
                time = "17:00",
                type = "fee",
                actionNeeded = "Pay semester tuition fee of Rs 45,000 on student portal before 5 PM to avoid fine.",
                confidence = "high"
            )
            imageUri.contains("exam", ignoreCase = true) -> NoticeData(
                title = "Mid-Term Examination Schedule - CS & EC",
                date = "2026-09-12",
                time = "09:30",
                type = "exam",
                actionNeeded = "Submit hall ticket form & bring valid college ID to Exam Hall 3.",
                confidence = "high"
            )
            imageUri.contains("low", ignoreCase = true) -> NoticeData(
                title = "Lab Exam Reschedule Notice (Blurry Photo)",
                date = "2026-09-08",
                time = "14:00",
                type = "exam",
                actionNeeded = "Check department notice board for revised slot allocation. Tap date to verify.",
                confidence = "low"
            )
            else -> NoticeData(
                title = "Smart India Hackathon 2026 - State Level Finals",
                date = "2026-09-15",
                time = "09:00",
                type = "event",
                actionNeeded = "Submit problem statement PPT to department coordinator.",
                confidence = "high"
            )
        }
    }
}
