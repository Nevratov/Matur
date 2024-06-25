package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class SendMessageResponse(
    @SerializedName("data") val message: MessageDto
)
