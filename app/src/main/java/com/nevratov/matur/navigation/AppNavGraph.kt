package com.nevratov.matur.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    startDestination: String,
    loginScreenContent: @Composable () -> Unit,
    exploreScreenContent: @Composable () -> Unit,
    matchesScreenContent: @Composable () -> Unit,
    chatListScreenContent: @Composable () -> Unit,
    chatScreenContent: @Composable (Int) -> Unit,
    profileScreenContent: @Composable () -> Unit,
    requestNameScreenContent: @Composable () -> Unit,
    requestDateScreenContent: @Composable () -> Unit,
    requestCityScreenContent: @Composable () -> Unit,
    requestEmailScreenContent: @Composable () -> Unit,
    registrationSuccessScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        builder = {
            registrationScreenNavGraph(
                requestNameScreenContent = requestNameScreenContent,
                requestDateScreenContent = requestDateScreenContent,
                requestCityScreenContent = requestCityScreenContent,
                requestEmailScreenContent = requestEmailScreenContent,
                registrationSuccessScreenContent = registrationSuccessScreenContent
            )
            composableWithTransition(
                route = Screen.Login.route,
                content = { loginScreenContent() }
            )
            composableWithTransition(
                route = Screen.Explore.route,
                content = { exploreScreenContent() }
            )
            composableWithTransition(
                route = Screen.Matches.route,
                content = { matchesScreenContent() }
            )
            composableWithTransition(
                route = Screen.ChatList.route,
                content = { chatListScreenContent() }
            )
            composableWithTransition(
                route = Screen.Chat.route,
                content = {
                    val dialogUserId = it.arguments?.getString(Screen.KEY_DIALOG_USER_ID)?.toInt()
                        ?: throw RuntimeException("argument  for ChatScreen when navigation not found")
                    chatScreenContent(dialogUserId)
                }
            )
            composableWithTransition(
                route = Screen.Profile.route,
                content = { profileScreenContent() }
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