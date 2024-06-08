package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class LikedUserDto(
    @SerializedName("liked_user_id") val userId: Int
)
