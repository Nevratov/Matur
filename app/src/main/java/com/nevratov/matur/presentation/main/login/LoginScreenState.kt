package com.nevratov.matur.presentation.main.login

sealed class LoginScreenState {

    data object Loading: LoginScreenState()

    data class Content(val email: String, val password: String): LoginScreenState()

    companion object {
        val initialState = Content("", "")
    }
}