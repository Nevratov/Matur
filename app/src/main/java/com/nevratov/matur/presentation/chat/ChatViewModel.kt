package com.nevratov.matur.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.ReceiveMessageUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val receiveMessageUseCase: ReceiveMessageUseCase
) : ViewModel() {

    private val webSocketClient = WebSocketClient

    private val userId = getUserUseCase().id

    private val interlocutorId = 4

    val chatScreenState = getMessagesByUserIdUseCase(id = interlocutorId)
        .map { ChatScreenState.Content(messages = it, userId = userId) }

    init {
        Log.d("User", userId.toString())
        webSocketClient.connect(listener = WebSocketListener(
            onMessageReceived = {
                receiveMessageUseCase(message = it)
            }
        ))
    }

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}