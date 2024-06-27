package com.nevratov.matur.presentation.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nevratov.matur.domain.entity.AuthState
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.NavigationState
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.MaturApplication
import com.nevratov.matur.presentation.NavigationItem
import com.nevratov.matur.presentation.chat.ChatScreen
import com.nevratov.matur.presentation.chat.ChatViewModel
import com.nevratov.matur.presentation.chat_list.ChatListScreen
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.explore.ExploreScreen
import com.nevratov.matur.presentation.explore.ExploreViewModel
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.presentation.main.login.LoginViewModel
import com.nevratov.matur.presentation.main.registration.RegistrationSuccessScreen
import com.nevratov.matur.presentation.main.registration.RegistrationViewModel
import com.nevratov.matur.presentation.main.registration.RequestCityScreen
import com.nevratov.matur.presentation.main.registration.RequestDateScreen
import com.nevratov.matur.presentation.main.registration.RequestEmailScreen
import com.nevratov.matur.presentation.main.registration.RequestNameScreen
import com.nevratov.matur.presentation.matches.MatchesScreen

lateinit var chatViewModel: ChatViewModel

@Composable
fun MainScreen(
    authState: State<AuthState>,
    exploreViewModel: ExploreViewModel,
    loginViewModel: LoginViewModel,
    registrationViewModel: RegistrationViewModel,
    chatListViewModel: ChatListViewModel
) {
    Log.d("MainScreen", "REC")

    val navigationState = rememberNavigationState()
    val startDestination = when (authState.value) {
        AuthState.Authorized -> {
            Screen.Explore.route
        }

        AuthState.NotAuthorized -> {
            Screen.Login.route
        }

        AuthState.Initial -> {
            return
        }
    }

    val component = (LocalContext.current.applicationContext as MaturApplication).component

    Scaffold(
        bottomBar = {
            val navBacStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
            val currentRoute = navBacStackEntry?.destination?.route
            if (authState.value == AuthState.Authorized && currentRoute != Screen.Chat.route) {
                BottomNavigationBar(navigationState)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .padding(16.dp)
        ) {
            AppNavGraph(
                navHostController = navigationState.navHostController,
                startDestination = startDestination,
                loginScreenContent = {
                    LoginScreen(
                        viewModel = loginViewModel,
                        createAccountClicked = { navigationState.navigateTo(Screen.RequestName.route) }
                    )
                },
                exploreScreenContent = { ExploreScreen(viewModel = exploreViewModel) },
                matchesScreenContent = { MatchesScreen() },
                chatListScreenContent = {
                    ChatListScreen(
                        viewModel = chatListViewModel,
                        onMessageItemClicked = {
                            chatViewModel = component.chatListComponentFactory().create(it.user.id)
                                .getViewModel()
                            navigationState.navigateToChat(Screen.Chat.route)
                        })
                },
                chatScreenContent = { ChatScreen(chatViewModel) },
                profileScreenContent = { },
                requestNameScreenContent = {
                    RequestNameScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestDate.route) }
                    )
                },
                requestDateScreenContent = {
                    RequestDateScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestCity.route) }
                    )
                },
                requestCityScreenContent = {
                    RequestCityScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateTo(Screen.RequestEmail.route) }
                    )
                },
                requestEmailScreenContent = {
                    RequestEmailScreen(
                        viewModel = registrationViewModel,
                        onNextClicked = { navigationState.navigateToRegistrationSuccess(Screen.RegistrationSuccess.route) }
                    )
                },
                registrationSuccessScreenContent = { RegistrationSuccessScreen() }
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    navigationState: NavigationState
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationItem.Explore,
        NavigationItem.Matches,
        NavigationItem.Chat,
        NavigationItem.Profile
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navigationState.navigateTo(item.screen.route)
                },
                label = { Text(stringResource(item.titleResId)) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.descriptionResId)
                    )
                }
            )
        }
    }
}