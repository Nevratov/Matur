package com.nevratov.matur.data.network

import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.LoginResponseDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.UsersToExploreResponseDto
import com.nevratov.matur.domain.entity.City
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("profile/login")
    suspend fun login(@Body loginData: LoginDataDto): Response<LoginResponseDto>

    @POST("profile/sign-up")
    suspend fun registerUser(@Body regUserInfo: RegUserInfoDto)

    @GET("city/get-by-name")
    suspend fun getCitiesByName(@Query("q") name: String): List<City>

    @Headers("Content-Type: application/json")
    @GET("like/user-list")
    suspend fun getUsersToExplore(@Header("Authorization") token: String): UsersToExploreResponseDto

    @POST("dislike/create")
    suspend fun dislike(
        @Header("Authorization") token: String,
        @Body dislikedUser: DislikedUserDto
    )

    @POST("like/create")
    suspend fun like(
        @Header("Authorization") token: String,
        @Body likedUser: LikedUserDto
    )

    @POST("im/create")
    suspend fun sendMessage(@Body message: MessageDto)

    @Headers("Content-Type: application/json")
    @POST("/im/messages")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Body messagesOptions: MessagesOptionsDto
    ): List<MessageDto>
}