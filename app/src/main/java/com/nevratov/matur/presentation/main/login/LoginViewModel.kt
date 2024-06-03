package com.nevratov.matur.presentation.main.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.repository.RepositoryImpl
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)

    fun login(email: String, password: String) {
        val loginData = LoginData(email = email, password = password)
        viewModelScope.launch {
            repository.login(loginData)
        }
    }
}