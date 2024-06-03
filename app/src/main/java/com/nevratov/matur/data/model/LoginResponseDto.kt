package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("user") val user: UserDto
)