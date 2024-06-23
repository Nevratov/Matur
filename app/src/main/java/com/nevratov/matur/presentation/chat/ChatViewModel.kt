package com.nevratov.matur.presentation.chat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.LoadNextMessagesUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import com.nevratov.matur.extentions.mergeWith
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val loadNextMessagesUseCase: LoadNextMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val receiverId: Int,
    private val application: Application
) : ViewModel() {

    private val loadNextMessagesFlow = MutableSharedFlow<ChatScreenState>()

    val chatScreenState = getMessagesByUserIdUseCase(id = receiverId)
        .map { ChatScreenState.Content(messages = it, userId = userId, receiverId = receiverId) }
        .mergeWith(loadNextMessagesFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatScreenState.Initial
        )

    private val userId = getUserUseCase().id

    fun sendMessage(message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(message)
        }
    }

    fun loadNextMessages() {
        val currentState = chatScreenState.value as ChatScreenState.Content
        if (!currentState.isNextMessages) return
        viewModelScope.launch {
            loadNextMessagesFlow.emit(currentState.copy(loadNextMessages = true, isNextMessages = true))
            val isNextMessages = loadNextMessagesUseCase(messagesWithId = receiverId)
            when (isNextMessages) {
                true -> {}
                false -> { loadNextMessagesFlow.emit(currentState.copy(isNextMessages = false)) }
            }
        }
    }

    fun showToast() {
        Toast.makeText(application, "Вы достигли конца диалога", Toast.LENGTH_LONG).show()
    }

}