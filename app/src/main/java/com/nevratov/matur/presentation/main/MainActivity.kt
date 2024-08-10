package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.getApplicationComponent
import com.nevratov.matur.presentation.login.MainLoginScreen
import com.nevratov.matur.presentation.login.LoginViewModel
import com.nevratov.matur.ui.theme.MaturTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var chatListViewModel: ChatListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            component.inject(this)

            val authState = mainViewModel.authState.collectAsState()
            MaturTheme {
                when (authState.value) {
                    AuthState.Authorized -> {
                        MainScreen(
                            chatListViewModel = chatListViewModel
                        )
                    }
                    AuthState.NotAuthorized -> {
                        MainLoginScreen(
                            loginViewModel = loginViewModel
                        )
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



