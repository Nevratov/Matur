package com.nevratov.matur.domain.entity

data class NetworkStatus(
    val userId: Int,
    val isOnline: Boolean,
    val isOpenedChatScreen: Boolean = false
)
