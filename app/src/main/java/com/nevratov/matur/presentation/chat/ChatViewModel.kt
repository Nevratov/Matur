package com.nevratov.matur.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase
) : ViewModel() {

    private val webSocketClient = WebSocketClient

    private val testId = 2

    val chatScreenState = getMessagesByUserIdUseCase(id = 2)
        .map { ChatScreenState.Content(messages = it) }

    init {
        webSocketClient.connect(listener = WebSocketListener(
            onMessageReceived = {

            }
        ))
    }

    suspend fun sendMessage(message: Message) {
        sendMessageUseCase(message)
    }
}