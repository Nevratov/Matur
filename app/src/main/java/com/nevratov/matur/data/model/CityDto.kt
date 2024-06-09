package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class CityDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("area") val area: String?,
    @SerializedName("region") val region: String?,
    @SerializedName("country") val country: String?,
)
