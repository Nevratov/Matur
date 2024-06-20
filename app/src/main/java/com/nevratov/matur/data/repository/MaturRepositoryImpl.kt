package com.nevratov.matur.data.repository

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener
import com.nevratov.matur.di.ApplicationScope
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.domain.entity.City
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@ApplicationScope
class MaturRepositoryImpl @Inject constructor(
    application: Application
) : MaturRepository {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    private val sharedPreferences = application.getSharedPreferences(USER_KEY, MODE_PRIVATE)

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
                emit(AuthState.Authorized)

                //Test FCM
                firebaseGetInstance()

            } else {
                emit(AuthState.NotAuthorized)
            }
        }
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
                    logoUrl = url
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
            Log.d("ExploreScreen", "Emitted: $testListExploreUsers")
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

    private val chatListRefreshEvents = MutableSharedFlow<Message>()

    // Get Messages for Chat Screen

    private val _chatMessages = mutableListOf<Message>()
    private val chatMessages: List<Message>
        get() = _chatMessages.toList()

    private val refreshMessagesEvents = MutableSharedFlow<Unit>()

    // Firebase Messaging

    private fun firebaseGetInstance() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "token = $token")
        })
    }


    // WebSocket

    private val webSocketClient = WebSocketClient

    private fun receiveMessage(message: Message) {
        coroutineScope.launch {
            _chatMessages.add(message)
            refreshMessagesEvents.emit(Unit)

            chatListRefreshEvents.emit(message)
        }
    }

    // Save User in cache with SharedPreferences

    private fun saveUserAndToken(user: User, token: String) {
        Log.d("User", user.toString())
        sharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            putString(USER_KEY, userJson)
            putString(TOKEN_KEY, token)
            apply()
        }
        Log.d("User", "auth = ${getToken()}")
        checkAuthState()
    }

    private fun getUserOrNull(): User? {
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return Gson().fromJson(userJson, User::class.java)
    }

    private fun getToken(): String {
        return sharedPreferences.getString(TOKEN_KEY, null)
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

    override fun getMessagesByUserId(id: Int) = flow {
        _chatMessages.clear()
        val messagesOptions = mapper.toMessagesOptionsDto(id)

        val messagesDto = apiService.getChatMessages(
            token = getToken(),
            messagesOptions = messagesOptions
        ).chatMessages


        val messages = mutableListOf<Message>()

        for (indexMessage in messagesDto.lastIndex downTo 0) {
            val message = messagesDto[indexMessage]
            messages.add(mapper.messageDtoToMessage(message))
        }

        _chatMessages.addAll(messages)
        emit(chatMessages)
        refreshMessagesEvents.collect {
            emit(chatMessages)
        }
    }

    override suspend fun sendMessage(message: Message) {
        apiService.sendMessage(
            token = getToken(),
            message = mapper.messageToCreateMessageDto(message)
        )

        val messageToSend = mapper.messageToSendMessageDto(message)
        val messageJson = Gson().toJson(messageToSend)
        webSocketClient.send(messageJson)

        _chatMessages.add(message.copy(id = _chatMessages.size))
        refreshMessagesEvents.emit(Unit)
    }

    override fun getChatList(): StateFlow<List<ChatListItem>> = flow {
        val chatListDto = apiService.getChatList(token = getToken()).chatList
        val downloadedChatList = mapper.chatListDtoToChatList(chatListDto)
        _chatList.addAll(downloadedChatList)
        emit(downloadedChatList)

        chatListRefreshEvents.collect { newMessage ->
            _chatList.apply {
                val chatListItem = find { it.user.id == newMessage.senderId }
                val newChatListItem = chatListItem?.copy(
                    message = newMessage.content,
                    timestamp = newMessage.timestamp
                )
                remove(chatListItem)
                newChatListItem?.let { add(it) }
            }
            emit(chatList.sortedByDescending { it.timestamp })
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override fun connectToWS() {
        webSocketClient.connect(
            listener = WebSocketListener(
                onMessageReceived = {
                    receiveMessage(message = it)
                },
                senderId = getUserOrNull()?.id ?: throw RuntimeException("User == null")
            )
        )
    }

    override suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

    override suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }

    override fun getUser(): User = getUserOrNull() ?: throw RuntimeException("User == null")

    companion object {
        private const val USER_KEY = "user_data"
        private const val TOKEN_KEY = "token"
    }
}