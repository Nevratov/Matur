package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ChatListItemDto(
    @SerializedName("content") val message: String,
    @SerializedName("created_at") val timestamp: Long,
    @SerializedName("user") val user: UserDto
)
