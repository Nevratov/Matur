package com.nevratov.matur.data.repository

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.di.ApplicationScope
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.domain.entity.City
import com.nevratov.matur.extentions.mergeWith
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ApplicationScope
class MaturRepositoryImpl @Inject constructor(
    application: Application
): MaturRepository {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    private val sharedPreferences = application.getSharedPreferences(USER_KEY, MODE_PRIVATE)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    // AuthState

    private val checkAuthStateEvents = MutableSharedFlow<Unit>(replay = 1)
    private val authStateFlow = flow {
        checkAuthStateEvents.emit(Unit)
        checkAuthStateEvents.collect {
            val user = getUser()
            val isLoggedIn = user != null
            if (isLoggedIn) emit(AuthState.Authorized) else emit(AuthState.NotAuthorized)
            Log.d("RepositoryImpl", "isLoggedIn = $isLoggedIn")
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    // ExploreUsers

    private var _exploreUsers = mutableListOf<User>()
    private val exploreUsers = _exploreUsers.toList()

    private val refreshExploreUsersEvents = MutableSharedFlow<List<User>>()
    private val checkExploreUsersEvents = MutableSharedFlow<Unit>(replay = 1)

    private val loadedExploreUsers = flow {
        checkExploreUsersEvents.emit(Unit)
        checkExploreUsersEvents.collect {
            val token = getToken()
            Log.d("ExploreScreen", "token = $token")
            Log.d("ExploreScreen", "до запроса лайков")
            val usersDto = apiService.getUsersToExplore(token = token)
            Log.d("ExploreScreen", "после запроса лайков")
            val users = mapper.listUserDtoToListUser(usersDto)
            Log.d("ExploreScreen", users.toString())
            _exploreUsers.apply {
                clear()
                addAll(users)
            }
            emit(exploreUsers)
        }
    }
//        .mergeWith(refreshExploreUsersEvents)
        .stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    // Save User in cache with SharedPreferences

    private fun saveUserAndToken(user: User, token: String) {
        sharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            putString(USER_KEY, userJson)
            putString(TOKEN_KEY, token)
            apply()
        }
       checkAuthState()
    }

    private fun getUser(): User? {
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
        apiService.dislike(
            token = getToken(),
            dislikedUser = mapper.userToDislikedUserDto(dislikedUser)
        )
        _exploreUsers.remove(dislikedUser)
        refreshExploreUsersEvents.emit(exploreUsers)
    }

    override suspend fun like(likedUser: User) {
        apiService.like(
            token = getToken(),
            likedUser = mapper.userToLikedUserDto(likedUser)
        )
        _exploreUsers.remove(likedUser)
        refreshExploreUsersEvents.emit(exploreUsers)
    }

    override suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

    override suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }

    companion object {
        private const val USER_KEY = "user_data"
        private const val TOKEN_KEY = "token"
    }
}