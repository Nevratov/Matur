package com.nevratov.matur.domain.entity

data class RegUserInfo(
    val name: String,
    val day: String,
    val month: String,
    val year: String,
    val gender: String,
    val email: String,
    val city: City?
) {
    companion object {
        val initial = RegUserInfo(
            name = "",
            day = "",
            month = "",
            year = "",
            gender = "",
            email = "",
            city = null
        )
    }
}