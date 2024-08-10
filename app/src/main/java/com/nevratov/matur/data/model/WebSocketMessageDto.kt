package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class WebSocketMessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("created_at") val timestamp: Long,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("type") val type: String,
)