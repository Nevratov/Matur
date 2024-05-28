package com.nevratov.matur.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    requestNameScreenContent: @Composable () -> Unit,
    requestDateScreenContent: @Composable () -> Unit,
    requestCityScreenContent: @Composable () -> Unit,
    requestEmailScreenContent: @Composable () -> Unit,
    registrationSuccessScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.RegistrationContainer.route,
        builder = {
            registrationScreenNavGraph(
                requestNameScreenContent = requestNameScreenContent,
                requestDateScreenContent = requestDateScreenContent,
                requestCityScreenContent = requestCityScreenContent,
                requestEmailScreenContent = requestEmailScreenContent,
                registrationSuccessScreenContent = registrationSuccessScreenContent
            )
        }
    )
}