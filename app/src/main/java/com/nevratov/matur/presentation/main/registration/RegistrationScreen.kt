package com.nevratov.matur.presentation.main.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState


@Composable
fun RegistrationScreen() {
    val navigationState = rememberNavigationState()
    val viewModel: RegistrationViewModel = viewModel()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            requestNameScreenContent = {
                RequestNameScreen(
                    paddingValues = paddingValues,
                    onNextOnClickListener = { firstName ->
                        viewModel.setName(firstName)
                        navigationState.navigateTo(Screen.RequestDate.route)
                    }
                )
            },
            requestDateScreenContent = {
                RequestDateScreen(
                    paddingValues = paddingValues,
                    onNextClickListener = { day, month, year, gender ->
                        viewModel.setBirthdayAndGender(day, month, year, gender)
                        navigationState.navigateTo(Screen.RequestCity.route)
                    }
                )
            },
            requestCityScreenContent = {
                RequestCityScreen(
                    paddingValues = paddingValues,
                    onCitySelected = {
                        navigationState.navigateTo(Screen.RequestEmail.route)
                    }
                )
            },
            requestEmailScreenContent = {
                RequestEmailScreen(
                    paddingValues = paddingValues,
                    onNextClickListener = { email ->
                        viewModel.setEmail(email)
                        navigationState.navigateToRegistrationSuccess(Screen.RegistrationSuccess.route)
                    },
                )
            },
            registrationSuccessScreenContent = {
                RegistrationSuccessScreen()
            }
        )
    }
}