package com.iqoo.noticesorter.model

import androidx.compose.ui.graphics.Color
import com.iqoo.noticesorter.ui.theme.*

enum class NoticeType(
    val label: String,
    val containerColor: Color,
    val contentColor: Color
) {
    EXAM(
        label = "Exam & Deadlines",
        containerColor = ExamAmberBg,
        contentColor = ExamAmber
    ),
    FEE(
        label = "Tuition & Fees",
        containerColor = FeeEmeraldBg,
        contentColor = FeeEmerald
    ),
    EVENT(
        label = "Campus Event",
        containerColor = EventPurpleBg,
        contentColor = EventPurple
    ),
    CIRCULAR(
        label = "Official Circular",
        containerColor = CircularBlueBg,
        contentColor = CircularBlue
    ),
    OTHER(
        label = "General Notice",
        containerColor = OtherSlateBg,
        contentColor = OtherSlate
    );

    companion object {
        fun fromString(value: String?): NoticeType {
            return when (value?.lowercase()?.trim()) {
                "exam" -> EXAM
                "fee" -> FEE
                "event" -> EVENT
                "circular" -> CIRCULAR
                else -> OTHER
            }
        }
    }
}

