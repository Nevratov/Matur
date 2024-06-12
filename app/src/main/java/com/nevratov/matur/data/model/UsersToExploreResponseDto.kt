package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class UsersToExploreResponseDto(
    @SerializedName("data") val listUsers: List<UserDto> 
)
