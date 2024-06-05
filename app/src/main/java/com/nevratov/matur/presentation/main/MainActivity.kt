package com.nevratov.matur.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.ui.theme.MaturTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val authState = viewModel.authState.collectAsState()

            MaturTheme {
                Log.d("RepositoryImpl", "State in MainActivity = ${authState.value}")
                when (authState.value) {
                    AuthState.Authorized -> {
                        MainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        LoginScreen(
                            paddingValues = PaddingValues(),
                            xxx = { viewModel.checkAuth() }
                        )
                    }
                    AuthState.Initial -> {}
                }

            }
        }
    }
}
