package com.nevratov.matur.presentation.chat

import com.nevratov.matur.domain.entity.User

sealed class ChatScreenState {

    data object Initial: ChatScreenState()

    data object Loading: ChatScreenState()

    data class Content(
        val messages: List<Message>,
        val userId: Int,
        val dialogUser: User,
        val onlineStatus: Boolean = false,
        val loadNextMessages: Boolean = false,
        val isNextMessages: Boolean = true
    ): ChatScreenState()
}