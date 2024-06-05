package com.nevratov.matur.presentation.main.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase
) : ViewModel() {

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
            loginUseCase(loginData)
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