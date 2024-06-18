package com.nevratov.matur.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.usecases.ConnectToWSUseCase
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val receiverId: Int
) : ViewModel() {

    private val userId = getUserUseCase().id

    val chatScreenState = getMessagesByUserIdUseCase(id = receiverId)
        .map { ChatScreenState.Content(messages = it, userId = userId, receiverId = receiverId) }


    fun sendMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }
}