package com.nevratov.matur.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun LoginNavGraph(
    navHostController: NavHostController,
    loginScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Login.route,
        builder = {
            composableWithTransition(
                route = Screen.Login.route,
                content = { loginScreenContent() }
            )
        }
    )
}

private fun NavGraphBuilder.composableWithTransition(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        content = content,
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(700)) },
    )
}