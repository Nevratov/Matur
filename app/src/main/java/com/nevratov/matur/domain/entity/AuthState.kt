package com.nevratov.matur.domain.entity

sealed class AuthState {

    data object Authorized : AuthState()

    data object NotAuthorized : AuthState()

    data object NotConnection : AuthState()

    data object Initial : AuthState()
}
