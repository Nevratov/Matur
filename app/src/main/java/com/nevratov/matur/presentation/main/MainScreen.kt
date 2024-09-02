package com.nevratov.matur.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.MaturApplication
import com.nevratov.matur.presentation.ViewModelFactory
import com.nevratov.matur.presentation.chat.ChatScreen
import com.nevratov.matur.presentation.chat.ChatViewModel
import com.nevratov.matur.presentation.chat_list.ChatListScreen
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.getApplicationComponent
import com.nevratov.matur.presentation.permissions.RequestNotificationPermission

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val component = getApplicationComponent()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            chatListScreenContent = {
                val viewModel: ChatListViewModel = viewModel(factory = component.getViewModelFactory())
                ChatListScreen(
                    viewModel = viewModel,
                    onMessageItemClicked = {
                        navigationState.navigateTo(Screen.Chat.getRouteWithArgs(it.user))
                    }
                )
            },
            chatScreenContent = { dialogUser ->
                val factory = component.getChatScreenComponentFactory().create(dialogUser).getViewModelFactory()
                val viewModel :ChatViewModel = viewModel(factory = factory)
                ChatScreen(
                    viewModel = viewModel,
                    onBackPressed = { navigationState.navHostController.popBackStack() }
                )
            },
        )
    }
    RequestNotificationPermission()
}



