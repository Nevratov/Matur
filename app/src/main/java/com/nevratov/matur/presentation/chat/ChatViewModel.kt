package com.nevratov.matur.presentation.chat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.di.ChatScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.LoadNextMessagesUseCase
import com.nevratov.matur.domain.usecases.ResetDialogOptionsUseCase
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

//@ChatScope
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val loadNextMessagesUseCase: LoadNextMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val resetDialogOptionsUseCase: ResetDialogOptionsUseCase,
    private val dialogUser: User,
    private val application: Application
) : ViewModel() {

    private val loadNextMessagesFlow = MutableSharedFlow<ChatScreenState>()

    val chatScreenState = getMessagesByUserIdUseCase(id = dialogUser.id)
        .map { ChatScreenState.Content(messages = it, userId = userId, receiverId = dialogUser.id) }
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
        if (!currentState.isNextMessages) {
            showToast()
            return
        }

        viewModelScope.launch {
            loadNextMessagesFlow.emit(currentState.copy(loadNextMessages = true))
            val isNextMessages = loadNextMessagesUseCase(messagesWithId = dialogUser.id)
            when (isNextMessages) {
                true -> {}
                false -> {
                    val newState = chatScreenState.value as ChatScreenState.Content
                    loadNextMessagesFlow.emit(newState.copy(loadNextMessages = false, isNextMessages = false))
                }
            }
        }
    }

    fun showToast() {
        Toast.makeText(application, "Вы достигли конца диалога", Toast.LENGTH_LONG).show()
    }


    public override fun onCleared() {
        Log.d("ChatScreen", "onCleared")
        super.onCleared()
        resetDialogOptionsUseCase()
    }
}