package com.nevratov.matur.data.network

import com.nevratov.matur.data.model.ChatListResponseDto
import com.nevratov.matur.data.model.ChatMessagesResponse
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.CreateNewFCMTokenDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.LoginResponseDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RemoveDialogDto
import com.nevratov.matur.data.model.RemoveMessageDto
import com.nevratov.matur.data.model.SendMessageResponse
import com.nevratov.matur.data.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

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

    @POST("im/create")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: CreateMessageDto
    ) : SendMessageResponse

    @POST("im/delete")
    suspend fun removeMessage(
        @Header("Authorization") token: String,
        @Body deleteMessage: RemoveMessageDto
    )

    @POST("firebase-token/create")
    suspend fun createNewFCMToken(
        @Header("Authorization") token: String,
        @Body newToken: CreateNewFCMTokenDto
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

    @POST("im/messages")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Body messagesOptions: MessagesOptionsDto
    ): ChatMessagesResponse

    @POST("profile/login")
    suspend fun login(@Body loginData: LoginDataDto): Response<LoginResponseDto>
}