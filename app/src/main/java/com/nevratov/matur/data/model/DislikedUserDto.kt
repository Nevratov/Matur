package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class DislikedUserDto(
    @SerializedName("disliked_user_id") val userId: Int
)
