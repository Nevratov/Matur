package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ChatListItemDto(
    @SerializedName("message") val message: MessageDto,
    @SerializedName("user") val user: UserDto
)
