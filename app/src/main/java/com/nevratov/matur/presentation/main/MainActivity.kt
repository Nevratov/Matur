package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.getApplicationComponent
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.explore.ExploreCard
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.presentation.main.login.LoginViewModel
import com.nevratov.matur.presentation.main.registration.RegistrationSuccessScreen
import com.nevratov.matur.presentation.main.registration.RegistrationViewModel
import com.nevratov.matur.presentation.main.registration.RequestCityScreen
import com.nevratov.matur.presentation.main.registration.RequestDateScreen
import com.nevratov.matur.presentation.main.registration.RequestEmailScreen
import com.nevratov.matur.presentation.main.registration.RequestNameScreen
import com.nevratov.matur.presentation.matches.MatchesScreen
import com.nevratov.matur.presentation.messages.MessagesScreen
import com.nevratov.matur.ui.theme.MaturTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val component = getApplicationComponent()
            component.inject(this)
            val authState = mainViewModel.authState.collectAsState()
            MaturTheme {
                StartApplication(
                    authState = authState,
                    loginViewModel = loginViewModel,
                    registrationViewModel = registrationViewModel
                )
            }
        }
    }
}



