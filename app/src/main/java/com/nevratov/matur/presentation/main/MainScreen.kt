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
import com.nevratov.matur.presentation.explore.ExploreScreen
import com.nevratov.matur.presentation.explore.ExploreViewModel
import com.nevratov.matur.presentation.matches.MatchesScreen
import com.nevratov.matur.presentation.permissions.RequestNotificationPermission
import com.nevratov.matur.presentation.profile.ProfileScreen

@Composable
fun MainScreen(
    exploreViewModel: ExploreViewModel,
    chatListViewModel: ChatListViewModel
) {

    val navigationState = rememberNavigationState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AppNavGraph(
            navHostController = navigationState.navHostController,
            exploreScreenContent = {
                ExploreScreen(
                    viewModel = exploreViewModel,
                    navigationState = navigationState
                )
            },
            matchesScreenContent = {
                MatchesScreen(
                    // Передать актуальный список совпавших пользователей
                    users = emptyList(),
                    navigationState = navigationState,
                    //Раскоментировать после передачи актуального списка
                    onMatchUserClicked = {
//                    chatViewModel = component.chatListComponentFactory().create(it.id).getViewModel()
//                    navigationState.navigateToChat(Screen.Chat.route)
                    })
            },
            chatListScreenContent = {
                ChatListScreen(
                    viewModel = chatListViewModel,
                    navigationState = navigationState,
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
            profileScreenContent = { ProfileScreen(navigationState = navigationState) },
        )
    }
    RequestNotificationPermission()
}



