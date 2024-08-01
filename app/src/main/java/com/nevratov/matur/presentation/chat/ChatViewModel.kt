package com.nevratov.matur.presentation.chat

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.usecases.BlockUserByIdUseCase
import com.nevratov.matur.domain.usecases.EditMessageUseCase
import com.nevratov.matur.domain.usecases.GetMessagesByUserIdUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import com.nevratov.matur.domain.usecases.LoadNextMessagesUseCase
import com.nevratov.matur.domain.usecases.OnlineStatusUseCase
import com.nevratov.matur.domain.usecases.RemoveDialogByIdUseCase
import com.nevratov.matur.domain.usecases.RemoveMessageUseCase
import com.nevratov.matur.domain.usecases.ResetDialogOptionsUseCase
import com.nevratov.matur.domain.usecases.SendMessageUseCase
import com.nevratov.matur.domain.usecases.SendTypingStatusUseCase
import com.nevratov.matur.domain.usecases.UnblockUserByIdUseCase
import com.nevratov.matur.extentions.mergeWith
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val removeMessageUseCase: RemoveMessageUseCase,
    private val editMessageUseCase: EditMessageUseCase,
    private val getMessagesByUserIdUseCase: GetMessagesByUserIdUseCase,
    private val loadNextMessagesUseCase: LoadNextMessagesUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val resetDialogOptionsUseCase: ResetDialogOptionsUseCase,
    private val onlineStatus: OnlineStatusUseCase,
    private val sendTypingStatusUseCase: SendTypingStatusUseCase,
    private val removeDialogByIdUseCase: RemoveDialogByIdUseCase,
    private val blockUserByIdUseCase: BlockUserByIdUseCase,
    private val unblockUserByIdUseCase: UnblockUserByIdUseCase,
    private val application: Application,
    private var dialogUser: User,
) : ViewModel() {

    private val screenStateRefreshFlow = MutableSharedFlow<ChatScreenState>()

    private var typingJob: Job? = null

    val chatScreenState = getMessagesByUserIdUseCase(id = dialogUser.id)
        .onStart { observeOnlineStatus() }
        .map {
            val status = onlineStatus.invoke().value
            Log.d("chatScreenState", it.toString())
            ChatScreenState.Content(
                messages = it,
                user = user,
                dialogUser = dialogUser,
                onlineStatus = status
            )
        }
        .mergeWith(screenStateRefreshFlow)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ChatScreenState.Initial
        )

    private val user = getUserUseCase()

    fun sendMessage(textMessage: String, replyMessage: Message? = null) {
        val currentTime = System.currentTimeMillis()
        val message = Message(
            id = 0,
            senderId = user.id,
            receiverId = dialogUser.id,
            content = textMessage,
            timestamp = currentTime,
            timestampEdited = currentTime,
            isRead = false,
            replyMessage = replyMessage
        )
        viewModelScope.launch {
            sendMessageUseCase(message)

            typingJob?.cancel()
            sendTypingStatusUseCase(isTyping = false, userId = user.id, dialogUserId = dialogUser.id)
        }

    }

    fun removeMessage(message: Message) {
        viewModelScope.launch {
            removeMessageUseCase(message)
        }
    }

    fun editMessage(message: Message) {
        viewModelScope.launch {
            editMessageUseCase(message)
        }
    }

    fun removeDialogById() {
        viewModelScope.launch {
            removeDialogByIdUseCase(id = dialogUser.id)
        }
    }

    fun blockUser() {
        val currentState = chatScreenState.value
        if (currentState !is ChatScreenState.Content) return
        viewModelScope.launch {
            blockUserByIdUseCase(id = dialogUser.id)
            dialogUser = dialogUser.copy(isBlocked = true)
            val newState = currentState.copy(dialogUser = dialogUser)
            screenStateRefreshFlow.emit(newState)
        }
    }

    fun unblockUser() {
        val currentState = chatScreenState.value
        if (currentState !is ChatScreenState.Content) return
        viewModelScope.launch {
            unblockUserByIdUseCase(id = dialogUser.id)
            dialogUser = dialogUser.copy(isBlocked = false)
            val newState = currentState.copy(dialogUser = dialogUser)
            screenStateRefreshFlow.emit(newState)
        }
    }

    fun loadNextMessages() {
        val currentState = chatScreenState.value as ChatScreenState.Content
        if (!currentState.isNextMessages) {
            showToast()
            return
        }

        viewModelScope.launch {
            screenStateRefreshFlow.emit(currentState.copy(loadNextMessages = true))
            val isNextMessages = loadNextMessagesUseCase(messagesWithId = dialogUser.id)
            when (isNextMessages) {
                true -> {}
                false -> {
                    val newState = chatScreenState.value as ChatScreenState.Content
                    screenStateRefreshFlow.emit(
                        newState.copy(
                            loadNextMessages = false,
                            isNextMessages = false
                        )
                    )
                }
            }
        }
    }

    fun typing() {
        val currentTypingJob = typingJob
        if (currentTypingJob != null && currentTypingJob.isActive) {
            typingJob?.cancel()
        } else {
            viewModelScope.launch {
                sendTypingStatusUseCase(isTyping = true, userId = user.id, dialogUserId = dialogUser.id)
            }
        }
        typingJob = viewModelScope.launch {
            delay(Duration.ofSeconds(3))
            sendTypingStatusUseCase(isTyping = false, userId = user.id, dialogUserId = dialogUser.id)
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

                if (status.isOnline) screenStateRefreshFlow.emit(currentState.copy(onlineStatus = status))
                else {
                    val currentTimestamp = System.currentTimeMillis()
                    screenStateRefreshFlow.emit(
                        currentState.copy(
                            onlineStatus = status,
                            dialogUser = dialogUser.copy(wasOnlineTimestamp = currentTimestamp)
                        )
                    )
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