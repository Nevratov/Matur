package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("auth_key") val token: String,
    @SerializedName("user") val user: UserDto
)