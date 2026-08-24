package com.iqoo.noticesorter.model

import androidx.compose.ui.graphics.Color

enum class NoticeType(
    val label: String,
    val containerColor: Color,
    val contentColor: Color
) {
    EXAM(
        label = "Exam",
        containerColor = Color(0xFFFFEBEE), // Coral red tint
        contentColor = Color(0xFFC62828)
    ),
    FEE(
        label = "Fee Deadline",
        containerColor = Color(0xFFFFF3E0), // Amber tint
        contentColor = Color(0xFFE65100)
    ),
    EVENT(
        label = "College Event",
        containerColor = Color(0xFFEDE7F6), // Indigo / Purple tint
        contentColor = Color(0xFF4527A0)
    ),
    CIRCULAR(
        label = "Official Circular",
        containerColor = Color(0xFFE0F2F1), // Teal tint
        contentColor = Color(0xFF00695C)
    ),
    OTHER(
        label = "General Notice",
        containerColor = Color(0xFFECEFF1), // Slate tint
        contentColor = Color(0xFF37474F)
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
