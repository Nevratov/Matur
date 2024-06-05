package com.nevratov.matur.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.repository.RepositoryImpl
import com.nevratov.matur.domain.repoository.Repository
import com.nevratov.matur.domain.usecases.CheckAuthUseCase
import com.nevratov.matur.domain.usecases.GetAuthStateFlowUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)
    val checkAuthUseCase = CheckAuthUseCase(repository)
    val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)

    init {
        checkAuth(delay = 0)
    }

    val authState = getAuthStateFlowUseCase()

    fun checkAuth(delay: Long = 1000) {
        viewModelScope.launch {
            delay(delay)
            checkAuthUseCase()
            Log.d("RepositoryImpl", "authState in viewModelChecked")
        }
    }
}