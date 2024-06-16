package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ChatListResponseDto(
    @SerializedName("data") val chatList: List<ChatListItemDto>
)
