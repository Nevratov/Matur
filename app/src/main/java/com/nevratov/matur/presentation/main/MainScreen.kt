package com.nevratov.matur.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.chat.ChatScreen
import com.nevratov.matur.presentation.chat_list.ChatListScreen
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.permissions.RequestNotificationPermission

@Composable
fun MainScreen(
    chatListViewModel: ChatListViewModel
) {

    val navigationState = rememberNavigationState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            chatListScreenContent = {
                ChatListScreen(
                    viewModel = chatListViewModel,
                    onMessageItemClicked = {
                        navigationState.navigateToChat(Screen.Chat.getRouteWithArgs(it.user))
                    })
            },
            chatScreenContent = {
                ChatScreen(
                    dialogUser = it,
                    onBackPressed = { navigationState.navHostController.popBackStack() }
                )
            },
        )
    }
    RequestNotificationPermission()
}



