package com.nevratov.matur.domain.entity

data class ChatListItem(
    val message: Message,
    val user: User,
    val isTyping: Boolean = false
)
