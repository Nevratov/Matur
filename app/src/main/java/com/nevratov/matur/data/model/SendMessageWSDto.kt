package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class SendMessageWSDto(
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("content") val message: String,
    @SerializedName("id") val id: Int = 0,
    @SerializedName("created_at") val timestamp: Long,
    @SerializedName("type") val type: String = "message",
)
