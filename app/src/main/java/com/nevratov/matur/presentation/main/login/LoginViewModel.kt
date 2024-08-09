package com.nevratov.matur.presentation.main.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.R
import com.nevratov.matur.domain.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val application: Application
) : ViewModel() {


    private val _screenState = MutableStateFlow<LoginScreenState>(LoginScreenState.initialState)
    val screenState = _screenState

    fun login(email: String, password: String) {
        val currentState = screenState.value
        if (currentState !is LoginScreenState.Content) return

        _screenState.value = LoginScreenState.Loading
        val loginData = LoginData(email = email, password = password)
        viewModelScope.launch {
            val answer = loginUseCase(loginData)
            if (!answer) Toast.makeText(
                application,
                application.getString(R.string.wrong_login_data_toast),
                Toast.LENGTH_LONG
            ).show()
            _screenState.value = currentState.copy(email = email, password = password)
        }
    }
}