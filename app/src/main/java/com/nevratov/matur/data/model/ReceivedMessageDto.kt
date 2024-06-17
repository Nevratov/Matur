package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ReceivedMessageDto(
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("content") val message: String
)
