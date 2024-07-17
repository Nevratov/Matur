package com.nevratov.matur.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nevratov.matur.navigation.LoginNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.presentation.main.login.LoginViewModel
import com.nevratov.matur.presentation.main.registration.RegistrationSuccessScreen
import com.nevratov.matur.presentation.main.registration.RegistrationViewModel
import com.nevratov.matur.presentation.main.registration.RequestCityScreen
import com.nevratov.matur.presentation.main.registration.RequestDateScreen
import com.nevratov.matur.presentation.main.registration.RequestEmailScreen
import com.nevratov.matur.presentation.main.registration.RequestNameScreen

@Composable
fun LoginMainScreen(
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
) {
    val navigationState = rememberNavigationState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoginNavGraph(
                navHostController = navigationState.navHostController,
                loginScreenContent = {
                    LoginScreen(
                        viewModel = loginViewModel,
                        createAccountClicked = { navigationState.navigateTo(Screen.RequestName.route) }
                    )
                },
                requestNameScreenContent = {
                    RequestNameScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestDate.route) }
                    )
                },
                requestDateScreenContent = {
                    RequestDateScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestCity.route) }
                    )
                },
                requestCityScreenContent = {
                    RequestCityScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestEmail.route) }
                    )
                },
                requestEmailScreenContent = {
                    RequestEmailScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateToRegistrationSuccess(Screen.RegistrationSuccess.route) }
                    )
                },
                registrationSuccessScreenContent = { RegistrationSuccessScreen() }
            )
        }
    }
}