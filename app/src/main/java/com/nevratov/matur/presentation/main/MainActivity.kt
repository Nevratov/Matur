package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.presentation.getApplicationComponent
import com.nevratov.matur.presentation.login.MainLoginScreen
import com.nevratov.matur.ui.theme.MaturTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            val mainViewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = mainViewModel.authState.collectAsState()
            MaturTheme {
                when (authState.value) {
                    AuthState.Authorized -> {
                        MainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        MainLoginScreen()
                    }
                    AuthState.NotConnection -> {
                        NotConnectionScreen()
                    }
                    AuthState.Initial -> {}
                }
            }
        }
    }
}


