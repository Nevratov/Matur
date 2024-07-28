package com.nevratov.matur.domain.repoository

import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.City
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MaturRepository {

    fun checkAuthState()

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun login(loginData: LoginData)

    suspend fun registration(regUserInfo: RegUserInfo)

    suspend fun getCitiesByName(name: String): List<City>

    suspend fun dislike(dislikedUser: User)

    suspend fun like(likedUser: User)

    fun getUsersToExplore(): StateFlow<User?>

    fun getChatByUserId(id: Int): Flow<List<Message>>

    suspend fun sendMessage(message: Message)

    fun getUser(): User

    suspend fun getUserById(id: Int): User

    fun getChatList(): StateFlow<List<ChatListItem>>

    fun onlineStatus(): StateFlow<OnlineStatus>

    suspend fun loadNextMessages(messagesWithId: Int): Boolean

    fun resetDialogOptions()

    fun createNewFCMToken(newToken: String)

    suspend fun removeMessage(message: Message)

    suspend fun editMessage(message: Message)

    suspend fun sendTypingStatus(isTyping: Boolean, userId: Int, dialogUserId: Int)
}