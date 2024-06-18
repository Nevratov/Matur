package com.nevratov.matur.presentation.chat_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nevratov.matur.domain.usecases.GetChatListUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    private val getChatListUseCase: GetChatListUseCase
): ViewModel() {

    val state = getChatListUseCase()
        .map {
            Log.d("webSocketTest", "map = $it")
            ChatListScreenState.Content(chatList = it)
        }
        .onStart { ChatListScreenState.Loading }
}