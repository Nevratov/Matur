package com.nevratov.matur.domain.repoository

import com.nevratov.matur.domain.entity.AuthState
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun checkAuthState()

    fun getAuthStateFlow(): StateFlow<AuthState>
}