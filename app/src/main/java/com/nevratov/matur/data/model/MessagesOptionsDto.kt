package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class MessagesOptionsDto(
    @SerializedName("receiver_id") val messagesWithUserId: String,
    @SerializedName("page_size") val pageSize: String,
    @SerializedName("page") val page: String
)
