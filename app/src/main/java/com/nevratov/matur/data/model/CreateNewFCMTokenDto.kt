package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class CreateNewFCMTokenDto(
    @SerializedName("token") val token: String
)
