package com.nevratov.matur.presentation.messages

data class UserMessageProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val logoUri: String,
    val lastMessage: String
)
