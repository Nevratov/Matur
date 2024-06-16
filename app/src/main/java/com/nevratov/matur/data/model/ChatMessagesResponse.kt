package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ChatMessagesResponse(
    @SerializedName("messages") val chatMessages: List<MessageDto>
)
