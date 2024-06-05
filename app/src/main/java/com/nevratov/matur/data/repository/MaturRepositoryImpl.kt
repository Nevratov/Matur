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
import com.nevratov.matur.presentation.main.registration.City
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

    suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

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

//    private val userChangedEvents = MutableSharedFlow<Unit>(replay = 1)
//    val userFlow: Flow<User> = flow {
//        userChangedEvents.collect {
//            val user = getUser()
//            emit(user)
//        }
//    }

    suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }

    private fun saveUser(user: User) {
        sharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            putString(USER_KEY, userJson)
            apply()
        }
       checkAuthState()
    }

    private fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_KEY, null)
        return Gson().fromJson(userJson, User::class.java)
    }

    // Implements
    override fun checkAuthState() {
        coroutineScope.launch {
            checkAuthStateEvents.emit(Unit)
        }
    }

    override fun login(loginData: LoginData) {
        coroutineScope.launch {
            val loginResponse = apiService.login(mapper.loginDataToLoginDataDto(loginData))
            if (!loginResponse.isSuccessful) return@launch
            val user = loginResponse.body()?.user ?: throw RuntimeException("user is null")
            saveUser(mapper.userDtoToUser(user))
        }
    }

    override fun getAuthStateFlow() = authStateFlow

    companion object {
        private const val USER_KEY = "user_data"
    }
}