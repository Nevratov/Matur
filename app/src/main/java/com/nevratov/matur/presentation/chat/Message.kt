package com.nevratov.matur.presentation.chat

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean
) {

    private val calendar = Calendar.getInstance().apply {
        time = Date(timestamp)
    }
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = dateFormat.format(calendar.time)
}

