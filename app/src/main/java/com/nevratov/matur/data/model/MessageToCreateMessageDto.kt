package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class CreateMessageDto(
    @SerializedName("receiver_id") val receiverId: String,
    @SerializedName("content") val message: String
)
