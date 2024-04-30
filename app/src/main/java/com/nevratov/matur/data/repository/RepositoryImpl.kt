package com.nevratov.matur.data.repository

import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import com.nevratov.matur.presentation.main.registration.RegistrationScreen

class RepositoryImpl {
    val apiService = ApiFactory.apiService

    suspend fun registration(regUserInfo: RegUserInfo) {

    }
}