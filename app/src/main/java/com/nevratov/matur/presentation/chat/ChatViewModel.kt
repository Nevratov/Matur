package com.nevratov.matur.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val webSocketClient = WebSocketClient

    private val userId = getUserUseCase().id
    private val interlocutorId = 4

    val chatScreenState = getMessagesByUserIdUseCase(id = 2)
        .map { ChatScreenState.Content(messages = it, userId = userId) }

    init {
        webSocketClient.connect(listener = WebSocketListener(
            onMessageReceived = {

            }
        ))
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}