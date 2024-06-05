package com.nevratov.matur.domain.repoository

import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.presentation.main.login.LoginData
import kotlinx.coroutines.flow.StateFlow

interface MaturRepository {

    fun checkAuthState()

    fun getAuthStateFlow(): StateFlow<AuthState>

    fun login(loginData: LoginData)
}