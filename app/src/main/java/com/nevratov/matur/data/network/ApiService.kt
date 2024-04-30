package com.nevratov.matur.data.network

import com.nevratov.matur.data.network.dto.RegUserInfoDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("user/create")
    suspend fun registerUser(@Body regUserInfo: RegUserInfoDto)
}