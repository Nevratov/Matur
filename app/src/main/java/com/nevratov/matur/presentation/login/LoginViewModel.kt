package com.nevratov.matur.presentation.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.LoginData
import com.nevratov.matur.domain.usecases.LoginUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val application: Application
) : ViewModel() {

    private val _screenState = MutableStateFlow<LoginScreenState>(LoginScreenState.initialState)
    val screenState = _screenState

    private var lastSuccessfulState = LoginScreenState.initialState
    private var toastConnectionLost: Toast? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        toastConnectionLost?.cancel()
        toastConnectionLost = Toast.makeText(
            application,
            application.getString(R.string.connection_lost_toast),
            Toast.LENGTH_SHORT
        )
        toastConnectionLost?.show()
        _screenState.value = lastSuccessfulState
    }


    fun login(email: String, password: String) {
        val currentState = screenState.value
        if (currentState !is LoginScreenState.Content) return
        lastSuccessfulState = currentState.copy(email = email, password)

        _screenState.value = LoginScreenState.Loading
        val loginData = LoginData(email = email, password = password)
        viewModelScope.launch(context = exceptionHandler) {
            val answer = loginUseCase(loginData)
            if (!answer) Toast.makeText(
                application,
                application.getString(R.string.wrong_login_data_toast),
                Toast.LENGTH_LONG
            ).show()
            _screenState.value = lastSuccessfulState
        }
    }
}