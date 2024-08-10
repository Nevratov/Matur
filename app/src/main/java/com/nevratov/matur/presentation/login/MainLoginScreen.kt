package com.nevratov.matur.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nevratov.matur.navigation.LoginNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState

@Composable
fun MainLoginScreen(
    loginViewModel: LoginViewModel,
) {
    val navigationState = rememberNavigationState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoginNavGraph(
                navHostController = navigationState.navHostController,
                loginScreenContent = {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onCreateAccountClicked = { navigationState.navigateTo(Screen.RequestName.route) }
                    )
                },
            )
        }
    }
}