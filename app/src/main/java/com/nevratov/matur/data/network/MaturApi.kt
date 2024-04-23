package com.nevratov.matur.data.network

import com.nevratov.matur.data.network.dto.RegUserInfo
import retrofit2.http.Body
import retrofit2.http.POST

interface MaturApi {

    @POST("user/create")
    suspend fun registerUser(@Body regUserInfo: RegUserInfo)
}