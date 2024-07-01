package com.nevratov.matur.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.registrationScreenNavGraph(
    requestNameScreenContent: @Composable () -> Unit,
    requestDateScreenContent: @Composable () -> Unit,
    requestCityScreenContent: @Composable () -> Unit,
    requestEmailScreenContent: @Composable () -> Unit,
    registrationSuccessScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.RequestName.route,
        route = Screen.RegistrationContainer.route,
        builder = {
            composable(
                route = Screen.RequestName.route,
                enterTransition = { fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                content = { requestNameScreenContent() }
            )
            composable(
                route = Screen.RequestDate.route,
                enterTransition = {fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                content = { requestDateScreenContent() }
            )
            composable(
                route = Screen.RequestCity.route,
                enterTransition = {fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                content = { requestCityScreenContent() }
            )
            composable(
                route = Screen.RequestEmail.route,
                enterTransition = {fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                content = { requestEmailScreenContent() }
            )
            composable(
                route = Screen.RegistrationSuccess.route,
                enterTransition = {fadeIn(animationSpec = tween(700)) },
                exitTransition = { fadeOut(animationSpec = tween(700)) },
                content = { registrationSuccessScreenContent() }
            )
        }
    )
}