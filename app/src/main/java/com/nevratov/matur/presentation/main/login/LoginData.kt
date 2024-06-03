package com.nevratov.matur.presentation.main.login

data class LoginData(
    val email: String,
    val password: String
) {

    companion object {

        val initial = LoginData(email = "", password = "")
    }
}
