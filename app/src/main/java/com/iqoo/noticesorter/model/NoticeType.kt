package com.iqoo.noticesorter.model

import androidx.compose.ui.graphics.Color
import com.iqoo.noticesorter.ui.theme.*

enum class NoticeType(
    val label: String,
    val containerColor: Color,
    val contentColor: Color
) {
    EXAM(
        label = "Exam & Tests",
        containerColor = Color(0xFFE8EEF5), // Soft Slate Blue tint
        contentColor = PaletteSlateBlue
    ),
    FEE(
        label = "Fee Deadline",
        containerColor = Color(0xFFF3EFDF), // Warm Cream tint
        contentColor = Color(0xFF7A6B3B)
    ),
    EVENT(
        label = "College Event",
        containerColor = Color(0xFFEFF4EC), // Soft Sage Green tint
        contentColor = PaletteMossGreen
    ),
    CIRCULAR(
        label = "Official Circular",
        containerColor = Color(0xFFEAF0F6), // Soft Steel tint
        contentColor = PaletteSlateBlue
    ),
    OTHER(
        label = "General Notice",
        containerColor = Color(0xFFF0F4F2), // Muted Moss tint
        contentColor = PaletteDarkText
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
