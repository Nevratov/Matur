package com.nevratov.matur.domain.entity

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
)
