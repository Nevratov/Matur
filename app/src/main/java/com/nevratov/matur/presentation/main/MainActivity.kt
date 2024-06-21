package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.explore.ExploreViewModel
import com.nevratov.matur.presentation.getApplicationComponent
import com.nevratov.matur.presentation.main.login.LoginViewModel
import com.nevratov.matur.presentation.main.registration.RegistrationViewModel
import com.nevratov.matur.ui.theme.MaturTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    @Inject
    lateinit var exploreViewModel: ExploreViewModel

    @Inject
    lateinit var chatListViewModel: ChatListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            component.inject(this)

            val authState = mainViewModel.authState.collectAsState()
            MaturTheme {
                MainScreen(
                    authState = authState,
                    exploreViewModel = exploreViewModel,
                    loginViewModel = loginViewModel,
                    registrationViewModel = registrationViewModel,
                    chatListViewModel = chatListViewModel
                )
            }
        }
    }
}



