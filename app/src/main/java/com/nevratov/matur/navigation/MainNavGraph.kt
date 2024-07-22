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
import com.google.gson.Gson
import com.nevratov.matur.domain.entity.User

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    exploreScreenContent: @Composable () -> Unit,
    matchesScreenContent: @Composable () -> Unit,
    chatListScreenContent: @Composable () -> Unit,
    chatScreenContent: @Composable (User) -> Unit,
    profileScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Explore.route,
        builder = {
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
                    val dialogUserJson = it.arguments?.getString(Screen.KEY_DIALOG_USER)
                        ?: throw RuntimeException("argument  for ChatScreen when navigation not found")
                    val dialogUser = Gson().fromJson(dialogUserJson, User::class.java)
                    chatScreenContent(dialogUser)
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