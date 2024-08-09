package com.nevratov.matur.domain.entity

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: Int,
    val content: String,
    val timestamp: Long,
    val timestampEdited: Long,
    val isRead: Boolean,
    val replyMessage: Message?
) {

    private val calendar = Calendar.getInstance().apply {
        time = Date(timestamp)
    }

    private val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
    val date: String = dateFormat.format(calendar.time)

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time: String = timeFormat.format(calendar.time)
}

