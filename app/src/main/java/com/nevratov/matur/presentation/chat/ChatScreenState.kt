package com.nevratov.matur.presentation.chat

sealed class ChatScreenState {

    data object Initial: ChatScreenState()

    data object Loading: ChatScreenState()

    data class Content(val messages: List<Message>): ChatScreenState()
}