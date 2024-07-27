package com.nevratov.matur.presentation.chat

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
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time: String = dateFormat.format(calendar.time)
}

