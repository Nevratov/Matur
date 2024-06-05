package com.nevratov.matur.presentation.main.login

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.repository.RepositoryImpl
import com.nevratov.matur.extentions.mergeWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)

    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Initial)
    val state = _state

    init {
        _state.value = LoginScreenState.Content("59aksai59@mail.ru", "nL8Uh9")
    }

    fun login() {
        val currentState = state.value
        if (currentState !is LoginScreenState.Content) return
        _state.value = LoginScreenState.Loading
        val loginData = LoginData(email = currentState.email, password = currentState.password)
        viewModelScope.launch {
            repository.login(loginData)
        }
    }

    fun changeEmail(email: String) {
        val currentState = _state.value
        if (currentState is LoginScreenState.Content) {
            _state.value = currentState.copy(email = email)
        }
    }

    fun changePassword(password: String) {
        val currentState = _state.value
        if (currentState is LoginScreenState.Content) {
            _state.value = currentState.copy(password = password)
        }
    }
}