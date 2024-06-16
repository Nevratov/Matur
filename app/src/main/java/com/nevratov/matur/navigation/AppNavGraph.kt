package com.nevratov.matur.navigation

import androidx.compose.runtime.Composable
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
    chatScreenContent: @Composable () -> Unit,
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
            composable(
                route = Screen.Login.route,
                content = { loginScreenContent() }
            )

            composable(
                route = Screen.Explore.route,
                content = { exploreScreenContent() }
            )
            composable(
                route = Screen.Matches.route,
                content = { matchesScreenContent() }
            )
            chatScreenNavGraph(
                chatListScreenContent = chatListScreenContent,
                chatScreenContent = chatScreenContent
            )
            composable(
                route = Screen.Profile.route,
                content = { profileScreenContent() }
            )
        }
    )
}