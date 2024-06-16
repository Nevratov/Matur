package com.nevratov.matur.presentation.chat_list

sealed class ChatListScreenState {

    data object Initial: ChatListScreenState()

    data object Loading: ChatListScreenState()

    data class Content(val chatList: List<ChatListItem>): ChatListScreenState()
}