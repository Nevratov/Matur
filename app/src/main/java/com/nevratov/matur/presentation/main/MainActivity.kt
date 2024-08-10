package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.presentation.ViewModelFactory
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.getApplicationComponent
import com.nevratov.matur.presentation.login.LoginViewModel
import com.nevratov.matur.presentation.login.MainLoginScreen
import com.nevratov.matur.ui.theme.MaturTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val chatListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatListViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val loginViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

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


