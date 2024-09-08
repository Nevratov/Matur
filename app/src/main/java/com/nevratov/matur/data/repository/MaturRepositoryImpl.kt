package com.nevratov.matur.data.repository

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.nevratov.matur.R
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiService
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.di.ApplicationScope
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.ChatListItem
import com.nevratov.matur.domain.entity.LoginData
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.TreeSet
import java.util.UUID
import javax.inject.Inject

@ApplicationScope
class MaturRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: Mapper,
    private val application: Application
) : MaturRepository {

    private val sharedPreferences =
        application.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    // AuthState

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            if (!isNetworkConnection()) {
                emit(AuthState.NotConnection)
                throw RuntimeException("There is no internet connection")
            }

            val user = getUserOrNull()
            val isLoggedIn = user != null
            if (isLoggedIn) {
                getDeviceUUID()
                connectToWS()
                getOnlineUsersId()
                firebaseGetInstance()
                emit(AuthState.Authorized)
            } else {
                emit(AuthState.NotAuthorized)
            }
        }
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    // ChatList Screen

    private val _chatList = TreeSet<ChatListItem>(compareBy { it.message.timestampEdited })
    private val chatList: List<ChatListItem>
        get() = _chatList.toList()

    private val chatListRefreshEvents = MutableSharedFlow<Unit>()
    private suspend fun refreshChatList(newMessage: Message) {
        _chatList.apply {
            val chatListItem = find {
                it.user.id == newMessage.senderId || it.user.id == newMessage.receiverId
            }
            val newChatListItem = chatListItem?.copy(
                message = newMessage,
            ) ?: ChatListItem(
                message = newMessage,
                user = getUserById(newMessage.senderId)
            )
            if (chatListItem != null) remove(chatListItem)
            add(newChatListItem)
        }
        chatListRefreshEvents.emit(Unit)
    }

    // Chat Screen

    private val _chatMessages = mutableListOf<Message>()
    private val chatMessages: List<Message>
        get() = _chatMessages.toList()

    private val refreshMessagesEvents = MutableSharedFlow<Unit>(replay = 1)

    private var dialogUserId: Int? = null
    private var dialogPage = DEFAULT_PAGE

    // User status (online / offline / typing)

    private val _onlineUsers = mutableMapOf<Int, Boolean>()
    private val onlineStatusRefreshFlow = MutableSharedFlow<OnlineStatus>()

    private val onlineStatusDialogUserStateFlow = flow {
        onlineStatusRefreshFlow.collect { newStatus ->
            _onlineUsers[newStatus.userId] = newStatus.isOnline
            if (newStatus.userId == dialogUserId) {
                emit(newStatus)
            }
            if (!newStatus.isOpenedChatScreen) {
                chatList.find { item -> item.user.id == newStatus.userId }?.let { item ->
                    val currentTimestamp = System.currentTimeMillis()
                    val newItem =
                        item.copy(
                            user = item.user.copy(
                                wasOnlineTimestamp = currentTimestamp
                            ),
                            isTyping = newStatus.isTyping
                        )
                    _chatList.remove(item)
                    _chatList.add(newItem)

                    chatListRefreshEvents.emit(Unit)
                }
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = EMPTY_ONLINE_STATUS
    )

    private suspend fun getOnlineUsersId() {
        val users = apiService.getOnlineUsersId(token = getToken())
        users.forEach { _onlineUsers[it] = true }
    }

    private fun checkOnlineStatusByUserId(id: Int): Boolean {
        val x = _onlineUsers[id] ?: false
        return x
    }


    // Network connection check

    private fun isNetworkConnection(): Boolean {
        val connectivityManager =
            getSystemService(application, ConnectivityManager::class.java) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // Firebase Cloud Messaging

    private val firebaseSharedPreferences =
        application.getSharedPreferences(FIREBASE_NAME, MODE_PRIVATE)

    private fun firebaseGetInstance() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            coroutineScope.launch {
                val token = task.result
                checkOnNewFCMToken(token)
            }
        })
    }

    private suspend fun checkOnNewFCMToken(newToken: String) {
        val oldToken = firebaseSharedPreferences.getString(FCM_TOKEN_KEY, null)
        if (oldToken == newToken) return

        apiService.createNewFCMToken(
            token = getToken(),
            newToken = mapper.tokenToCreateFCMTokenDto(newToken)
        )
        firebaseSharedPreferences.edit().apply {
            putString(FCM_TOKEN_KEY, newToken)
            apply()
        }
    }


    // WebSocket

    private val webSocketClient = WebSocketClient

    private suspend fun connectToWS() {
        //todo
        webSocketClient.disconnect()
        _chatList.clear()
        _chatMessages.clear()

        webSocketClient.connect(
            listener = WebSocketListener(
                onMessageReceived = { message ->
                    receiveMessage(message = message)
                    if (dialogUserId != message.senderId) sendNotificationNewMessage(message)
                },
                onStatusReceived = { status ->
                    coroutineScope.launch {
                        onlineStatusRefreshFlow.emit(status)
                    }
                },
                onEditMessageReceived = { message ->
                    coroutineScope.launch {
                        refreshChatList(message)
                        if (message.senderId == dialogUserId) {
                            val editedMessage = chatMessages.find { it.id == message.id }
                            val indexEditedMessage = chatMessages.indexOf(editedMessage)
                            if (indexEditedMessage == UNKNOWN_ITEM) return@launch
                            _chatMessages[indexEditedMessage] = message
                            refreshMessagesEvents.emit(Unit)
                        }
                    }
                },
                onUserIdReadAllMessages = { id ->
                    coroutineScope.launch {
                        if (id == dialogUserId) {
                            val readMessages = mutableListOf<Message>()
                            _chatMessages.forEach { oldMessage ->
                                readMessages.add(oldMessage.copy(isRead = true))
                            }
                            _chatMessages.clear()
                            _chatMessages.addAll(readMessages)
                            refreshMessagesEvents.emit(Unit)
                        }
                        val chatListItem = chatList.find { it.user.id == id }
                        chatListItem?.let { item ->
                            val newChatListItem =
                                item.copy(message = item.message.copy(isRead = true))
                            _chatList.remove(item)
                            _chatList.add(newChatListItem)
                            chatListRefreshEvents.emit(Unit)
                        }
                    }
                },
                senderId = getUserOrNull()?.id ?: throw RuntimeException("User == null"),
                uuid = getDeviceUUID()
            )
        )
    }

    private fun receiveMessage(message: Message) {
        coroutineScope.launch {
            if (dialogUserId == message.senderId) {
                _chatMessages.add(index = 0, element = message)
                refreshMessagesEvents.emit(Unit)

                val readMessage = mapper.readMessageToWebSocketMessageDto(
                    userId = message.receiverId,
                    dialogUserId = message.senderId
                )
                val readMessageJson = Gson().toJson(readMessage)
                webSocketClient.send(readMessageJson)
                refreshChatList(message.copy(isRead = true))
            } else {
                refreshChatList(message)
            }
        }
    }

// Notifications

    private fun sendNotificationNewMessage(message: Message) {
        coroutineScope.launch {
            val sender = getUserById(message.senderId)

            val imageRequest = ImageRequest.Builder(application.applicationContext)
                .data(sender.logoUrl)
                .build()

            val imageResult =
                (application.applicationContext.imageLoader.execute(imageRequest) as SuccessResult).drawable
            val bitmap = (imageResult as BitmapDrawable).bitmap

            val notificationBuilder =
                Notification.Builder(application.applicationContext, CHANEL_ID)
                    .setSmallIcon(R.drawable.matur_ico)
                    .setContentTitle("${sender.name} · Matur")
                    .setContentText(message.content)
                    .setLargeIcon(bitmap)

            val notificationManager =
                getSystemService(application.applicationContext, NotificationManager::class.java)

            val channel = NotificationChannel(
                CHANEL_ID,
                CHANEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager?.createNotificationChannel(channel)
            notificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    // UUID

    private fun getDeviceUUID(): String {
        var uuid = sharedPreferences.getString(UUID_KEY, null)
        if (uuid == null) {
            uuid = generateUUID()
            sharedPreferences.edit().putString(UUID_KEY, uuid).apply()
        }
        return uuid
    }

    private fun generateUUID(): String = UUID.randomUUID().toString()

    // Save User in cache with SharedPreferences

    private val userSharedPreferences = application.getSharedPreferences(USER_KEY, MODE_PRIVATE)

    private fun saveUserAndToken(user: User, token: String) {
        userSharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            putString(USER_KEY, userJson)
            putString(TOKEN_KEY, token)
            apply()
        }
        coroutineScope.launch {
            checkAuthState()
        }
    }

    private fun getUserOrNull(): User? {
        val userJson = userSharedPreferences.getString(USER_KEY, null)
        return Gson().fromJson(userJson, User::class.java)
    }

    private fun getToken(): String {
        return userSharedPreferences.getString(TOKEN_KEY, null)
            ?: throw RuntimeException("authKey == null")
    }

// Implementations

    override fun getAuthStateFlow() = authStateFlow

    override suspend fun checkAuthState() {
        checkAuthStateEvents.emit(Unit)
    }

    override suspend fun login(loginData: LoginData): Boolean {
        val loginResponse = apiService.login(mapper.loginDataToLoginDataDto(loginData))

        if (loginResponse.isSuccessful) {
            val response = loginResponse.body() ?: throw RuntimeException("response == null")
            val userDto = response.user
            val token = response.token
            saveUserAndToken(user = mapper.userDtoToUser(userDto), token = token)
            return true
        } else {
            return false
        }
    }

    override fun getChatByUserId(id: Int) = flow {
        resetDialogOptions()
        dialogUserId = id

        val onlineStatus = OnlineStatus(
            userId = id,
            isOnline = checkOnlineStatusByUserId(id),
            isOpenedChatScreen = true
        )
        onlineStatusRefreshFlow.emit(onlineStatus)

        loadNextMessages(messagesWithId = id)
        refreshChatList(newMessage = chatMessages.first()) // remove new message mark

        refreshMessagesEvents.collect {
            emit(chatMessages)
        }
    }

    override suspend fun loadNextMessages(messagesWithId: Int): Boolean {
        val options = mapper.toMessagesOptionsDto(
            messagesWithUserId = messagesWithId,
            page = dialogPage++
        )

        val messagesDto = apiService.getChatMessages(
            token = getToken(),
            messagesOptions = options
        ).chatMessages

        if (messagesDto.size == _chatMessages.size) return false

        val messages = mutableListOf<Message>()
        messagesDto.forEach {
            messages.add(mapper.messageDtoToMessage(it))
        }
        _chatMessages.clear()
        _chatMessages.addAll(messages)

        refreshMessagesEvents.emit(Unit)
        return true
    }

    override suspend fun sendMessage(message: Message) {
        val response = apiService.sendMessage(
            token = getToken(),
            message = mapper.messageToCreateMessageDto(message)
        )

        val messageToSend = mapper.messageDtoToWebSocketMessageDto(
            message = response.message,
            uuid = getDeviceUUID()
        )

        val messageJson = Gson().toJson(messageToSend)
        webSocketClient.send(messageJson)

        val messageWithId = mapper.messageDtoToMessage(response.message)

        _chatMessages.add(index = 0, element = messageWithId)
        refreshMessagesEvents.emit(Unit)
        refreshChatList(messageWithId)
    }

    override suspend fun removeMessage(message: Message, removeEveryone: Boolean) {
        apiService.removeMessage(
            token = getToken(),
            deleteMessage = mapper.messageIdToRemoveMessageDto(
                id = message.id,
                removeEveryone = removeEveryone
            )
        )
        _chatMessages.remove(message)
        refreshMessagesEvents.emit(Unit)

        val chatListItem = chatList.find { it.user.id == dialogUserId }
        chatListItem?.let { item ->
            if (item.message.id == message.id) {
                val newChatListItem = item.copy(message = chatMessages.first())
                _chatList.remove(item)
                _chatList.add(newChatListItem)
                chatListRefreshEvents.emit(Unit)
            }
        }
    }

    override suspend fun editMessage(message: Message) {
        apiService.editMessage(
            token = getToken(),
            editMessage = mapper.messageToEditMessageDto(message = message)
        )
        val index = chatMessages.indexOfFirst { it.id == message.id }
        if (index != -1) {
            _chatMessages[index] = message
            refreshMessagesEvents.emit(Unit)
        }
        val chatListItem = chatList.find { it.user.id == dialogUserId }
        chatListItem?.let { item ->
            if (item.message.id == message.id) {
                val newChatListItem = item.copy(message = message)
                _chatList.remove(item)
                _chatList.add(newChatListItem)
                chatListRefreshEvents.emit(Unit)
            }
        }
    }

    override fun getChatList(): StateFlow<List<ChatListItem>> = flow {
        val chatListDto = apiService.getChatList(token = getToken()).chatList
        val downloadedChatList = mapper.chatListDtoToChatList(chatListDto)
        _chatList.addAll(downloadedChatList)
        emit(downloadedChatList)

        chatListRefreshEvents.collect {
            emit(chatList.sortedByDescending { it.message.timestamp })
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun sendTypingStatus(isTyping: Boolean, userId: Int, dialogUserId: Int) {
        val typingToSend = mapper.typingToWebSocketMessageDto(
            isTyping = isTyping,
            userId = userId,
            dialogUserId = dialogUserId
        )

        val typingJson = Gson().toJson(typingToSend)
        webSocketClient.send(typingJson)
    }

    override fun onlineStatus(): StateFlow<OnlineStatus> = onlineStatusDialogUserStateFlow

    override fun getUser(): User = getUserOrNull() ?: throw RuntimeException("User == null")

    override suspend fun getUserById(id: Int): User {
        return mapper.userDtoToUser(apiService.getUserById(id = id, token = getToken()))
    }

    override fun resetDialogOptions() {
        _chatMessages.clear()
        dialogPage = DEFAULT_PAGE
        dialogUserId = null
    }

    override suspend fun removeDialogById(id: Int, removeEveryone: Boolean) {
        apiService.removeDialogByUserId(
            token = getToken(),
            removeDialog = mapper.idToRemoveDialogDto(id = id, removeEveryone = removeEveryone)
        )
        _chatList.removeIf { it.user.id == id }
        chatListRefreshEvents.emit(Unit)
    }

    override suspend fun blockUserById(id: Int) {
        apiService.blockUserById(token = getToken(), id = id)
        val chatItem =
            chatList.find { it.user.id == id } ?: throw RuntimeException("chatItem == null")
        val newItem = chatItem.copy(user = chatItem.user.copy(isBlocked = true))
        _chatList.remove(chatItem)
        _chatList.add(newItem)
        chatListRefreshEvents.emit(Unit)
    }

    override suspend fun unblockUserById(id: Int) {
        apiService.unblockUserById(token = getToken(), id = id)
        val chatItem =
            chatList.find { it.user.id == id } ?: throw RuntimeException("chatItem == null")
        val newItem = chatItem.copy(user = chatItem.user.copy(isBlocked = false))
        _chatList.remove(chatItem)
        _chatList.add(newItem)
        chatListRefreshEvents.emit(Unit)
    }

    override fun createNewFCMToken(newToken: String) {
        coroutineScope.launch {
            apiService.createNewFCMToken(
                token = getToken(),
                newToken = mapper.tokenToCreateFCMTokenDto(newToken)
            )
        }
    }

    companion object {
        private val EMPTY_ONLINE_STATUS = OnlineStatus(
            userId = 0,
            isOnline = false,
            isTyping = false,
            isOpenedChatScreen = false
        )

        private const val SHARED_PREFERENCES_NAME = "app_data"
        private const val USER_KEY = "user_data"
        private const val TOKEN_KEY = "token"
        private const val UUID_KEY = "uuid"
        const val FIREBASE_NAME = "firebase"
        const val FCM_TOKEN_KEY = "fcm_token"
        private const val DEFAULT_PAGE = 1
        private const val RETRY_TIMEOUT_MILLIS = 1000L
        private const val CHANEL_ID = "30"
        private const val CHANEL_NAME = "receive_message"
        private const val NOTIFICATION_ID = 40
        private const val UNKNOWN_ITEM = -1
    }
}