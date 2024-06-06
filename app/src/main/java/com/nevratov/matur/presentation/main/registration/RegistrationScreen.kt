package com.nevratov.matur.presentation.main.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.explore.ExploreCard
import com.nevratov.matur.presentation.main.MainScreenState
import com.nevratov.matur.presentation.main.login.LoginScreen
import com.nevratov.matur.presentation.matches.MatchesScreen
import com.nevratov.matur.presentation.messages.MessagesScreen


//@Composable
//fun RegistrationScreen() {
//    val mainScreenState = remember {
//        mutableStateOf<MainScreenState>(MainScreenState.Initial)
//    }
//
//    val navigationState = rememberNavigationState()
//    val viewModel: RegistrationViewModel = viewModel()
//
//    Scaffold(
//        modifier = Modifier
//            .background(MaterialTheme.colorScheme.background)
//            .padding(16.dp),
//        bottomBar = {
//            when (mainScreenState.value) {
//                MainScreenState.Home -> {
//                    BottomAppBar {
//                        Text(text = "1")
//                        Text(text = "2")
//                        Text(text = "3")
//                    }
//                }
//                else -> {}
//            }
//        }
//    ) { paddingValues ->
//        AppNavGraph(
//            navHostController = navigationState.navHostController,
//            loginScreenContent = {
////                LoginScreen(viewModel = )
//            },
//            exploreScreenContent = {
//                ExploreCard()
//            },
//            matchesScreenContent = {
//                MatchesScreen()
//            },
//            messagesScreenContent = {
//                MessagesScreen()
//            },
//            requestNameScreenContent = {
//                RequestNameScreen(
////                    paddingValues = paddingValues,
//                    onNextOnClickListener = { firstName ->
//                        viewModel.setName(firstName)
//                        navigationState.navigateTo(Screen.RequestDate.route)
//                    }
//                )
//            },
//            requestDateScreenContent = {
//                RequestDateScreen(
//                    paddingValues = paddingValues,
//                    onNextClickListener = { day, month, year, gender ->
//                        viewModel.setBirthdayAndGender(day, month, year, gender)
//                        navigationState.navigateTo(Screen.RequestCity.route)
//                    }
//                )
//            },
//            requestCityScreenContent = {
//                RequestCityScreen(
//                    paddingValues = paddingValues,
//                    onCitySelected = {
//                        viewModel.setCity(it)
//                        navigationState.navigateTo(Screen.RequestEmail.route)
//                    }
//                )
//            },
//            requestEmailScreenContent = {
//                RequestEmailScreen(
//                    paddingValues = paddingValues,
//                    onNextClickListener = { email ->
//                        viewModel.setEmail(email)
//                        navigationState.navigateToRegistrationSuccess(Screen.RegistrationSuccess.route)
//                    },
//                )
//            },
//            registrationSuccessScreenContent = {
//                RegistrationSuccessScreen()
//            }
//        )
//    }
//}