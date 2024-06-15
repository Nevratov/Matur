package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class ImagesSizeDto(
    @SerializedName("images_size") val sizes: SizeDto
)
