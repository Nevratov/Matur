package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val timestampCreateSec: Long,
    @SerializedName("updated_at") val timestampUpdateSec: Long,
    @SerializedName("is_read") val isRead: Int,
    @SerializedName("reply_id") val replyId: Int?
)
