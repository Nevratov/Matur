package com.nevratov.matur.domain.entity

data class OnlineStatus(
    val userId: Int,
    val isOnline: Boolean = false,
    val isTyping: Boolean = false,
    val isOpenedChatScreen: Boolean = false
)
