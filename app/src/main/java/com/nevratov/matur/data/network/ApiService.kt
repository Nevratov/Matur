package com.nevratov.matur.data.network

import com.nevratov.matur.data.model.ChatListResponseDto
import com.nevratov.matur.data.model.ChatMessagesResponse
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.CreateNewFCMTokenDto
import com.nevratov.matur.data.model.RemoveMessageDto
import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.LoginResponseDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.RemoveDialogDto
import com.nevratov.matur.data.model.SendMessageResponse
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.model.UsersToExploreResponseDto
import com.nevratov.matur.domain.entity.City
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: CreateMessageDto
    ) : SendMessageResponse

    @POST("im/messages")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Body messagesOptions: MessagesOptionsDto
    ): ChatMessagesResponse

    @GET("im")
    suspend fun getChatList(
        @Header("Authorization") token: String
    ) : ChatListResponseDto

    @GET("user/view/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): UserDto

    @GET("user/online")
    suspend fun getOnlineUsersId(@Header("Authorization") token: String): List<Int>

    @POST("firebase-token/create")
    suspend fun createNewFCMToken(
        @Header("Authorization") token: String,
        @Body newToken: CreateNewFCMTokenDto
    )

    @POST("im/delete")
    suspend fun removeMessage(
        @Header("Authorization") token: String,
        @Body deleteMessage: RemoveMessageDto
    )

    @POST("im/delete")
    suspend fun removeDialogByUserId(
        @Header("Authorization") token: String,
        @Body removeDialog: RemoveDialogDto
    )

    @POST("user/block/{id}")
    suspend fun blockUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

    @POST("user/unblock/{id}")
    suspend fun unblockUserById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )
}