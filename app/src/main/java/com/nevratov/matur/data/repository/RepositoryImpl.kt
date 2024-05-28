package com.nevratov.matur.data.repository

import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.presentation.main.registration.City
import com.nevratov.matur.presentation.main.registration.RegUserInfo

class RepositoryImpl {
    private val apiService = ApiFactory.apiService
    private val mapper = Mapper()

    suspend fun registration(regUserInfo: RegUserInfo) {
        apiService.registerUser(mapper.regUserInfoToRegUserInfoDto(regUserInfo))
    }

    suspend fun getCitiesByName(name: String): List<City> {
        return apiService.getCitiesByName(name)
    }
}