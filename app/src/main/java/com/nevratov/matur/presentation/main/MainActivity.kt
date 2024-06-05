package com.nevratov.matur.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.getApplicationComponent
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.presentation.main.login.LoginViewModel
import com.nevratov.matur.ui.theme.MaturTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val component = getApplicationComponent()
            component.inject(this)

            val authState = viewModel.authState.collectAsState()
            MaturTheme {
                when (authState.value) {
                    AuthState.Authorized -> {
                        MainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        LoginScreen(viewModel = loginViewModel)
                    }
                    AuthState.Initial -> {}
                }
            }
        }
    }
}
