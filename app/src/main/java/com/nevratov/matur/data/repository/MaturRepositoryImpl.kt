package com.nevratov.matur.data.repository

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.nevratov.matur.R
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.model.RemoveDialogDto
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.di.ApplicationScope
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.City
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.RegUserInfo
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
import javax.inject.Inject
import kotlin.random.Random

@ApplicationScope
class MaturRepositoryImpl @Inject constructor(
    private val application: Application
) : MaturRepository {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    private val userSharedPreferences = application.getSharedPreferences(USER_KEY, MODE_PRIVATE)
    private val firebaseSharedPreferences = application.getSharedPreferences(FIREBASE_NAME, MODE_PRIVATE)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    // AuthState

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val user = getUserOrNull()
            val isLoggedIn = user != null
            if (isLoggedIn) {
                connectToWS()
                getOnlineUsersId()
                emit(AuthState.Authorized)
                //Test FCM
                firebaseGetInstance()
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

    // ExploreUsers

    private val _testListExploreUsers = mutableListOf<User>().apply {

        repeat(19) {

            val url = if (Random.nextBoolean()) {
                "https://i.pinimg.com/736x/e8/45/14/e84514699a36c54b570359d52b0ef83f.jpg"
            } else {
                "https://celes.club/uploads/posts/2021-11/1638298686_53-celes-club-p-malenkii-barashek-zhivotnie-krasivo-foto-58.jpg"
            }

            add(
                User(
                    id = it,
                    name = "Надя $it",
                    gender = "",
                    birthday = "",
                    wasOnlineTimestamp = 0,
                    cityId = 1,
                    aboutMe = "Очень красивая девушка твоей мечты. Шлю $it воздушных поцелуев",
                    height = 160,
                    weight = 50,
                    bodyType = "",
                    education = "",
                    job = "",
                    maritalStatus = "",
                    children = "",
                    house = "1",
                    nationality = "",
                    languageSkills = "",
                    religion = "",
                    religiosityLevel = "",
                    expectations = "",
                    drinking = "",
                    smoking = "",
                    logoUrl = url,
                    isBlocked = false
                )
            )
        }
    }
    private val testListExploreUsers: List<User>
        get() = _testListExploreUsers


//    private var _exploreUsers = mutableListOf<User>()
//    private val exploreUsers: List<User>
//        get() = _exploreUsers

    private val refreshExploreUsersEvents = MutableSharedFlow<Unit>(replay = 1)
    private val loadedExploreUsers = flow {
//            val token = getToken()
//            val usersResponse = apiService.getUsersToExplore(token = token)
//            val usersDto = usersResponse.listUsers
//            val users = mapper.listUserDtoToListUser(usersDto)
//            _exploreUsers.apply {
//                clear()
//                addAll(users)
//            }
//            emit(exploreUsers)
        refreshExploreUsersEvents.emit(Unit)
        refreshExploreUsersEvents.collect {
//            emit(listOf())
//            delay(10)

            emit(testListExploreUsers.firstOrNull())
        }
    }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    // ChatList Screen Items

    private val _chatList = mutableListOf<ChatListItem>()
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
            )
            remove(chatListItem)
            newChatListItem?.let { add(index = 0, element = it) }
            Log.d("refreshChatList", _chatList.toString())
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
            Log.d("chatScreenState", "collect")
            _onlineUsers[newStatus.userId] = newStatus.isOnline
            if (newStatus.userId == dialogUserId) {
                emit(newStatus)
            }
            if (!newStatus.isOpenedChatScreen) {
                chatList.find { item -> item.user.id == newStatus.userId }?.let { item ->
                    val currentTimestamp = System.currentTimeMillis()
                    val itemIndex = chatList.indexOf(item)
                    val newItem =
                        item.copy(user = item.user.copy(wasOnlineTimestamp = currentTimestamp))
                    _chatList[itemIndex] = newItem
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


    // Firebase Cloud Messaging

    private fun firebaseGetInstance() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            coroutineScope.launch {
                val token = task.result
                Log.d("FCM", "task.isSuccessful | token = $token")
                checkOnNewFCMToken(token)
            }
        })
    }

    private suspend fun checkOnNewFCMToken(newToken: String) {
        val oldToken = firebaseSharedPreferences.getString(FCM_TOKEN_KEY, null)
        Log.d("FCM", "check token, old token = $oldToken")
        if (oldToken == newToken) return

        apiService.createNewFCMToken(token = getToken(), newToken = mapper.stringToCreateFCMTokenDto(newToken))
        firebaseSharedPreferences.edit().apply {
            putString(FCM_TOKEN_KEY, newToken)
            apply()
        }
        Log.d("FCM", "new Token is REGISTER")
    }


    // WebSocket

    private val webSocketClient = WebSocketClient

    private suspend fun connectToWS() {
        webSocketClient.connect(
            listener = WebSocketListener(
                onMessageReceived = { message ->
                    receiveMessage(message = message)
                    if (dialogUserId != message.senderId) sendNotificationNewMessage(message)
                },
                onStatusReceived = { status ->
                    Log.d("chatScreenState", "recived: $status")
                    coroutineScope.launch {
                        onlineStatusRefreshFlow.emit(status)
                    }
                },
                onUserIdReadAllMessages = { id ->
                    if (id == dialogUserId) {
                        val readMessages = mutableListOf<Message>()
                        _chatMessages.forEach { oldMessage ->
                            readMessages.add(oldMessage.copy(isRead = true))
                        }
                        _chatMessages.clear()
                        _chatMessages.addAll(readMessages)
                        coroutineScope.launch {
                            refreshMessagesEvents.emit(Unit)
                        }
                    }
                },
                senderId = getUserOrNull()?.id ?: throw RuntimeException("User == null")
            )
        )
    }

    private fun receiveMessage(message: Message) {
        coroutineScope.launch {
            if (dialogUserId == message.senderId) {
                _chatMessages.add(index = 0, element = message)
                refreshMessagesEvents.emit(Unit)
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

// Save User in cache with SharedPreferences

    private fun saveUserAndToken(user: User, token: String) {
        userSharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            putString(USER_KEY, userJson)
            putString(TOKEN_KEY, token)
            apply()
        }
        checkAuthState()
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

    override fun checkAuthState() {
        coroutineScope.launch {
            checkAuthStateEvents.emit(Unit)
        }
    }

    override fun login(loginData: LoginData) {
        coroutineScope.launch {
            val loginResponse = apiService.login(mapper.loginDataToLoginDataDto(loginData))
            if (!loginResponse.isSuccessful) return@launch
            val userDto = loginResponse.body()?.user ?: throw RuntimeException("user is null")
            val token = loginResponse.body()?.token ?: throw RuntimeException("token is null")
            saveUserAndToken(user = mapper.userDtoToUser(userDto), token = token)
        }
    }

    override fun getUsersToExplore() = loadedExploreUsers

    override suspend fun dislike(dislikedUser: User) {
//        apiService.dislike(
//            token = getToken(),
//            dislikedUser = mapper.userToDislikedUserDto(dislikedUser)
//        )
//        _exploreUsers.remove(dislikedUser)
//        refreshExploreUsersEvents.emit(exploreUsers)
        _testListExploreUsers.remove(dislikedUser)
        refreshExploreUsersEvents.emit(Unit)
    }

    override suspend fun like(likedUser: User) {
//        apiService.like(
//            token = getToken(),
//            likedUser = mapper.userToLikedUserDto(likedUser)
//        )
//        _exploreUsers.remove(likedUser)
//        refreshExploreUsersEvents.emit(exploreUsers)


        _testListExploreUsers.remove(likedUser)
        refreshExploreUsersEvents.emit(Unit)
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
        refreshChatList(newMessage = chatMessages.first())

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

        Log.d("sendMessage", "response = $response")

        // TODO Catch server error response
        val messageToSend = mapper.messageDtoToWebSocketMessageDto(response.message)

        val messageJson = Gson().toJson(messageToSend)
        webSocketClient.send(messageJson)

        val messageWithId = mapper.messageDtoToMessage(response.message)

        _chatMessages.add(index = 0, element = messageWithId)
        refreshMessagesEvents.emit(Unit)
        refreshChatList(messageWithId)
        Log.d("sendMessage", "Message sending")
    }

    override suspend fun removeMessage(message: Message) {
        apiService.removeMessage(
            token = getToken(),
            deleteMessage = mapper.messageIdToDeleteMessageDto(id = message.id)
        )
        _chatMessages.remove(message)
        refreshMessagesEvents.emit(Unit)
    }

    override suspend fun editMessage(message: Message) {
        val index = chatMessages.indexOfFirst { it.id == message.id }
        if (index != -1) {
            _chatMessages[index] = message
            refreshMessagesEvents.emit(Unit)
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
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
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
        Log.d("sendTypingStatus", "typing json = $typingJson")
    }

    override fun onlineStatus(): StateFlow<OnlineStatus> = onlineStatusDialogUserStateFlow

    override suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

    override suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }

    override fun getUser(): User = getUserOrNull() ?: throw RuntimeException("User == null")

    override suspend fun getUserById(id: Int): User {
        return mapper.userDtoToUser(apiService.getUserById(id = id, token = getToken()))
    }

    override fun resetDialogOptions() {
        _chatMessages.clear()
        dialogPage = DEFAULT_PAGE
        dialogUserId = null
    }

    override suspend fun removeDialogById(id: Int) {
        apiService.removeDialogByUserId(
            token = getToken(),
            removeDialog = mapper.idToRemoveDialogDto(id)
        )
    }

    override suspend fun blockUserById(id: Int) {
        apiService.blockUserById(token = getToken(), id = id)
        val chatItem = chatList.find { it.user.id == id } ?: throw RuntimeException("chatItem == null")
        val newItem = chatItem.copy(user = chatItem.user.copy(isBlocked = true))
        _chatList.remove(chatItem)
        _chatList.add(newItem)
        chatListRefreshEvents.emit(Unit)
    }

    override suspend fun unblockUserById(id: Int) {
        apiService.unblockUserById(token = getToken(), id = id)
        val chatItem = chatList.find { it.user.id == id } ?: throw RuntimeException("chatItem == null")
        val newItem = chatItem.copy(user = chatItem.user.copy(isBlocked = false))
        _chatList.remove(chatItem)
        _chatList.add(newItem)
        chatListRefreshEvents.emit(Unit)
    }

    override fun createNewFCMToken(newToken: String) {
        coroutineScope.launch {
            apiService.createNewFCMToken(
                token = getToken(),
                newToken = mapper.stringToCreateFCMTokenDto(newToken)
            )
        }
    }

    companion object {
        private val EMPTY_ONLINE_STATUS =  OnlineStatus(
            userId = 0,
            isOnline = false,
            isTyping = false,
            isOpenedChatScreen = false
        )

        private const val USER_KEY = "user_data"
        private const val TOKEN_KEY = "token"
        const val FIREBASE_NAME = "firebase"
        const val FCM_TOKEN_KEY = "fcm_token"
        private const val DEFAULT_PAGE = 1
        private const val RETRY_TIMEOUT_MILLIS = 1000L
        private const val CHANEL_ID = "30"
        private const val CHANEL_NAME = "receive_message"
        private const val NOTIFICATION_ID = 40
    }
}