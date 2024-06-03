package com.nevratov.matur.data.network

import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.LoginResponseDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.presentation.main.registration.City
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("user/login")
    suspend fun login(@Body loginData: LoginDataDto): Response<LoginResponseDto>

    @POST("user/sign-up")
    suspend fun registerUser(@Body regUserInfo: RegUserInfoDto)

    @GET("city/get-by-name")
    suspend fun getCitiesByName(@Query("q") name: String): List<City>

    @POST("im/create")
    suspend fun sendMessage(@Body message: MessageDto)

    @GET("messages/{userId}")
    suspend fun getMessages(@Path("userId") userId: String): List<MessageDto>
}