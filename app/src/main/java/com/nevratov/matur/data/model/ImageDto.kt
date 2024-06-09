package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("images_size") val imagesSize: ImagesSizeDto
)
