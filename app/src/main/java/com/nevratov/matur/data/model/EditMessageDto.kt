package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class EditMessageDto(
    @SerializedName("id") val messageId: Int,
    @SerializedName("content") val content: String
)
