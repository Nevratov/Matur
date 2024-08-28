package com.nevratov.matur.domain.repoository

import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.ChatListItem
import com.nevratov.matur.domain.entity.LoginData
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MaturRepository {

    fun getUser(): User

    fun resetDialogOptions()

    fun createNewFCMToken(newToken: String)

    fun onlineStatus(): StateFlow<OnlineStatus>

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun getChatByUserId(id: Int): Flow<List<Message>>

    fun getChatList(): StateFlow<List<ChatListItem>>

    suspend fun checkAuthState()

    suspend fun blockUserById(id: Int)

    suspend fun unblockUserById(id: Int)

    suspend fun removeDialogById(id: Int, removeEveryone: Boolean)

    suspend fun getUserById(id: Int): User

    suspend fun sendMessage(message: Message)

    suspend fun editMessage(message: Message)

    suspend fun removeMessage(message: Message)

    suspend fun login(loginData: LoginData): Boolean

    suspend fun loadNextMessages(messagesWithId: Int): Boolean

    suspend fun sendTypingStatus(isTyping: Boolean, userId: Int, dialogUserId: Int)
}