package com.nevratov.matur.data.model

data class MessageDto(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean
)
