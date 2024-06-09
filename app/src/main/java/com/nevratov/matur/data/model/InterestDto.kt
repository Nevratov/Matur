package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class InterestDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)
