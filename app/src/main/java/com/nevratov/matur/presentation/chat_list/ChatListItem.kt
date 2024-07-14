package com.nevratov.matur.presentation.chat_list

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message

data class ChatListItem(
    val message: Message,
    val user: User
)
