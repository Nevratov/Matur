package com.nevratov.matur.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.usecases.CheckAuthUseCase
import com.nevratov.matur.domain.usecases.ConnectToWSUseCase
import com.nevratov.matur.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val checkAuthUseCase: CheckAuthUseCase,
    getAuthStateFlowUseCase: GetAuthStateFlowUseCase

): ViewModel() {

    init {
        checkAuth()
    }

    val authState = getAuthStateFlowUseCase()

    private fun checkAuth() {
        viewModelScope.launch {
            checkAuthUseCase()
        }
    }
}