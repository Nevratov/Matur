package com.nevratov.matur.data.repository

import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import com.nevratov.matur.presentation.main.registration.RegistrationScreen

class RepositoryImpl {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.RegUserInfoToRegUserInfoDto(regUserInfo))
    }
}