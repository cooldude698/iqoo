package com.iqoo.noticesorter.data

import com.iqoo.noticesorter.model.NoticeData
import kotlinx.coroutines.delay

interface NoticeProcessor {
    suspend fun processNotice(imageUri: String): NoticeData
}

/**
 * Mock Notice Processor for UI development & testing on feature/app-ui-calendar.
 * Simulates real OCR + LLM processing speed and returns realistic test notices.
 */
class MockNoticeProcessor : NoticeProcessor {
    override suspend fun processNotice(imageUri: String): NoticeData {
        // Simulate OCR + LLM latency (1.5 seconds)
        delay(1500)

        // Select sample notice based on URI or return default mock
        return when {
            imageUri.contains("fee", ignoreCase = true) -> NoticeData(
                title = "Even Semester Tuition Fee Payment",
                date = "2026-09-05",
                time = "17:00",
                type = "fee",
                action_needed = "Pay semester tuition fee of Rs 45,000 on student portal before 5 PM to avoid fine.",
                confidence = "high"
            )
            imageUri.contains("event", ignoreCase = true) -> NoticeData(
                title = "Tech Fest Hackathon Registration",
                date = "2026-08-30",
                time = "10:00",
                type = "event",
                action_needed = "Register team of 3 on college portal for 24-hour hackathon track.",
                confidence = "high"
            )
            imageUri.contains("low", ignoreCase = true) -> NoticeData(
                title = "Lab Exam Reschedule Notice",
                date = "",
                time = null,
                type = "exam",
                action_needed = "Check department notice board for revised slot allocation.",
                confidence = "low"
            )
            else -> NoticeData(
                title = "Mid-Term Examination Schedule - CS & EC",
                date = "2026-09-12",
                time = "09:30",
                type = "exam",
                action_needed = "Submit hall ticket form & bring valid college ID to Exam Hall 3.",
                confidence = "high"
            )
        }
    }
}
