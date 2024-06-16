package com.nevratov.matur.presentation.chat_list

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val logoUri: String,
    val lastMessage: String
)
