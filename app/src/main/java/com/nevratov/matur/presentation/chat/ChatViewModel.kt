package com.nevratov.matur.presentation.chat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.LoadNextMessagesUseCase
import com.nevratov.matur.domain.usecases.OnlineStatusUseCase
import com.nevratov.matur.domain.usecases.ResetDialogOptionsUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import com.nevratov.matur.extentions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val loadNextMessagesUseCase: LoadNextMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val resetDialogOptionsUseCase: ResetDialogOptionsUseCase,
    private val onlineStatus: OnlineStatusUseCase,
    private val application: Application,
    private val dialogUser: User,
) : ViewModel() {

    private val loadNextMessagesFlow = MutableSharedFlow<ChatScreenState>()
    private val onlineStatusRefreshFlow = MutableSharedFlow<ChatScreenState>()


    val chatScreenState = getMessagesByUserIdUseCase(id = dialogUser.id)
        .onStart { observeOnlineStatus() }
        .map {
            val status = onlineStatus.invoke().value
            Log.d("chatScreenState", it.toString())
            ChatScreenState.Content(
                messages = it,
                userId = userId,
                dialogUser = dialogUser,
                onlineStatus = status
            )
        }
        .mergeWith(loadNextMessagesFlow)
        .mergeWith(onlineStatusRefreshFlow)
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
                    loadNextMessagesFlow.emit(
                        newState.copy(
                            loadNextMessages = false,
                            isNextMessages = false
                        )
                    )
                }
            }
        }
    }

    fun showToast() {
        Toast.makeText(application, "Вы достигли конца диалога", Toast.LENGTH_LONG).show()
    }

    private fun observeOnlineStatus() {
        viewModelScope.launch {
            onlineStatus().collect { status ->
                val currentState = chatScreenState.value
                if (currentState !is ChatScreenState.Content) return@collect

                if (status.isOnline) onlineStatusRefreshFlow.emit(currentState.copy(onlineStatus = status))
                else {
                    val currentTimestamp = System.currentTimeMillis()
                    onlineStatusRefreshFlow.emit(currentState.copy(
                        onlineStatus = status,
                        dialogUser = dialogUser.copy(wasOnlineTimestamp = currentTimestamp)
                    ))
                }
            }
        }
    }

    public override fun onCleared() {
        Log.d("ChatScreen", "onCleared")
        super.onCleared()
        resetDialogOptionsUseCase()
    }
}