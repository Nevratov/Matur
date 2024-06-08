package com.nevratov.matur.presentation.main.registration

import com.nevratov.matur.domain.entity.City

data class RegUserInfo(
    var name: String,
    var day: String,
    var month: String,
    var year: String,
    var gender: String,
    var email: String,
    var city: City?
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