package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class RegUserInfoDto(
    val name: String,
    val gender: String,
    val email: String,
    val birthday: String,
    @SerializedName("city_id") val cityId: String
)