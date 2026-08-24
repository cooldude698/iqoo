package com.example.noticesorter.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NoticeData(
    val title: String,
    val date: String?,
    val time: String? = null,
    val type: String, // exam | fee | event | circular | other
    @SerialName("action_needed")
    val actionNeeded: String,
    val confidence: String // high | low
)
