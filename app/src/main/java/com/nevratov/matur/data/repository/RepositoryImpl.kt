package com.nevratov.matur.data.repository

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.City
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RepositoryImpl(application: Application) {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    private val sharedPreferences = application.getSharedPreferences(USER_KEY, MODE_PRIVATE)

    suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

    suspend fun login(loginData: LoginData) {
        val loginResponse = apiService.login(mapper.loginDataToLoginDataDto(loginData))
        if (!loginResponse.isSuccessful) return
        val user = loginResponse.body()?.user ?: throw RuntimeException("user is null")
        saveUser(mapper.userDtoToUser(user))
        Log.d("RepositoryImpl", user.toString())
    }

    suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }

    private fun saveUser(user: User) {
        sharedPreferences.edit().apply {
            val userJson = Gson().toJson(user)
            Log.d("RepositoryImpl", userJson)
            putString(USER_KEY, userJson)
            apply()
        }
    }

    private fun getUser(): User {
        val userJson = sharedPreferences.getString(USER_KEY, null)
            ?: throw RuntimeException("Обработать если нет сохранённого юзера")
        return Gson().fromJson(userJson, User::class.java)
    }

    companion object {
        private const val USER_KEY = "user_data"
    }
}