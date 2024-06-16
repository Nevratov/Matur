package com.nevratov.matur.presentation.chat_list

import android.icu.util.Calendar
import com.google.gson.annotations.SerializedName
import com.nevratov.matur.domain.entity.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatListItem(
    val message: String,
    val timestamp: Long,
    val user: User
) {

    private val calendar = Calendar.getInstance().apply {
        time = Date(timestamp)
    }
    private val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = dateFormat.format(calendar.time)
}
