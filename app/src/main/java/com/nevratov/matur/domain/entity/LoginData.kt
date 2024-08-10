package com.nevratov.matur.domain.entity

data class LoginData(
    val email: String,
    val password: String
) {

    companion object {

        val initial = LoginData(email = "", password = "")
    }
}
