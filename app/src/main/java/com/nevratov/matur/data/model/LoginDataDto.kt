package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class LoginDataDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)
