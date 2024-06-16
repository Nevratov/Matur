package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("receiver_id") val receiverId: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val timestamp: Long,
    @SerializedName("is_read") val isRead: Int
)
