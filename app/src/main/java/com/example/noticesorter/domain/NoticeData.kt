package com.example.noticesorter.domain

import kotlinx.serialization.Serializable

@Serializable
data class NoticeData(
    val title: String,
    val date: String,
    val time: String? = null,
    val type: String, // exam | fee | event | circular | other
    val action_needed: String,
    val confidence: String // high | low
)
