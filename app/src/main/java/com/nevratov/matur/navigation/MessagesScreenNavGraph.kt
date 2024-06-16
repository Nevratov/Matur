package com.nevratov.matur.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.chatScreenNavGraph(
    chatListScreenContent: @Composable () -> Unit,
    chatScreenContent: @Composable () -> Unit,
) {
    navigation(
        startDestination = Screen.ChatList.route,
        route = Screen.ChatContainer.route,
        builder = {
            composable(
                route = Screen.ChatList.route,
                content = { chatListScreenContent() }
            )
            composable(
                route = Screen.Chat.route,
                content = { chatScreenContent() }
            )
        }
    )
}