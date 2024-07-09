package com.nevratov.matur.data.network

import com.nevratov.matur.data.model.ChatListResponseDto
import com.nevratov.matur.data.model.ChatMessagesResponse
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.LoginResponseDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.SendMessageResponse
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.model.UsersToExploreResponseDto
import com.nevratov.matur.domain.entity.City
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
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

    @Headers("Content-Type: application/json")
    @POST("im/create")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: CreateMessageDto
    ) : SendMessageResponse

    @Headers("Content-Type: application/json")
    @POST("im/messages")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Body messagesOptions: MessagesOptionsDto
    ): ChatMessagesResponse

    @Headers("Content-Type: application/json")
    @GET("im")
    suspend fun getChatList(
        @Header("Authorization") token: String
    ) : ChatListResponseDto

    @GET("user/view/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): UserDto

    @Headers("Content-Type: application/json")
    @GET("user/online")
    suspend fun getOnlineUsersId(@Header("Authorization") token: String): List<Int>
}