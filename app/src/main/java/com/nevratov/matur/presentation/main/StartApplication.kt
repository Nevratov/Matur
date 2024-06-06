package com.nevratov.matur.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.nevratov.matur.domain.entity.AuthState
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

@Composable
fun StartApplication(
    authState: State<AuthState>,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel
) {
    val navigationState = rememberNavigationState()
    val startDestination = when (authState.value) {
        AuthState.Authorized -> {
            Screen.Explore.route
        }

        AuthState.NotAuthorized, AuthState.Initial -> {
            Screen.Login.route
        }
    }
    AppNavGraph(
        navHostController = navigationState.navHostController,
        startDestination = startDestination,
        loginScreenContent = { LoginScreen(viewModel = loginViewModel) },
        exploreScreenContent = { ExploreCard() },
        matchesScreenContent = { MatchesScreen() },
        messagesScreenContent = { MessagesScreen() },
        requestNameScreenContent = {
            RequestNameScreen(viewModel = registrationViewModel)
            navigationState.navigateTo(Screen.RequestDate.route)
        },
        requestDateScreenContent = {
            RequestDateScreen(viewModel = registrationViewModel)
            navigationState.navigateTo(Screen.RequestCity.route)
        },
        requestCityScreenContent = {
            RequestCityScreen(viewModel = registrationViewModel)
            navigationState.navigateTo(Screen.RequestEmail.route)
        },
        requestEmailScreenContent = {
            RequestEmailScreen(viewModel = registrationViewModel)
            navigationState.navigateToRegistrationSuccess(Screen.RegistrationSuccess.route)
        },
        registrationSuccessScreenContent = { RegistrationSuccessScreen() }
    )
}