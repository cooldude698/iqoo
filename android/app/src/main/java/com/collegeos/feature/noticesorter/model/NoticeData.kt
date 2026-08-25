package com.collegeos.feature.noticesorter.model

import java.io.Serializable
import kotlinx.serialization.Serializable as KotlinxSerializable
import kotlinx.serialization.SerialName

/**
 * Shared JSON Contract between Prit's OCR+LLM pipeline (feature/ocr-llm-pipeline)
 * and Aman's UI & Calendar layer (feature/app-ui-calendar).
 */
@KotlinxSerializable
data class NoticeData(
    val title: String,
    val date: String?, // YYYY-MM-DD format
    val time: String? = null, // HH:MM in 24hr format or null
    val type: String, // "exam" | "fee" | "event" | "circular" | "other"
    @SerialName("action_needed")
    val actionNeeded: String,
    val confidence: String = "high" // "high" | "low"
) : Serializable {

    val noticeTypeEnum: NoticeType
        get() = NoticeType.fromString(type)

    val isLowConfidence: Boolean
        get() = confidence.lowercase() == "low" || date.isNullOrBlank() || date == "null"
}
