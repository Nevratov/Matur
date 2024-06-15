package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class SizeDto(
    @SerializedName("128") val urlSquare128: String,
    @SerializedName("512") val urlSquare512: String,
    @SerializedName("1024") val urlSquare1024: String,
    @SerializedName("original") val urlOriginal: String
)
