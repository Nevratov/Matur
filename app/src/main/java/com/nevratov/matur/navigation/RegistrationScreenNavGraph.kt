package com.nevratov.matur.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.registrationScreenNavGraph(
    requestNameScreenContent: @Composable () -> Unit,
    requestDateScreenContent: @Composable () -> Unit,
    requestEmailScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.RequestName.route,
        route = Screen.RegistrationContainer.route,
        builder = {
            composable(
                route = Screen.RequestName.route,
                content = { requestNameScreenContent() }
            )
            composable(
                route = Screen.RequestDate.route,
                content = { requestDateScreenContent() }
            )
            composable(
                route = Screen.RequestEmail.route,
                content = { requestEmailScreenContent() }
            )
        }
    )
}