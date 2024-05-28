package com.nevratov.matur.presentation.main.registration

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
)
