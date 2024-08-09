package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("auth_key") val token: String,
    @SerializedName("user") val user: UserDto
)