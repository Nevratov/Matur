package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class DeleteMessageDto(
    @SerializedName("im_id") val messageId: Int,
)
