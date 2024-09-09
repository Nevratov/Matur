package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class WebSocketMessageDto(
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("created_at") val timestamp: Long? = null,
    @SerializedName("uuid") val uuid: String? = null,
)