package com.nevratov.matur.presentation.chat

import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User

sealed class ChatScreenState {

    data object Initial: ChatScreenState()

    data object Loading: ChatScreenState()

    data class Content(
        val messages: List<Message>,
        val user: User,
        val dialogUser: User,
        val onlineStatus: OnlineStatus,
        val loadNextMessages: Boolean = false,
        val isNextMessagesExist: Boolean = true
    ): ChatScreenState()
}