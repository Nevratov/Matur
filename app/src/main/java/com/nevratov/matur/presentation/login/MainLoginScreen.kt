package com.nevratov.matur.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nevratov.matur.navigation.LoginNavGraph
import com.nevratov.matur.navigation.rememberNavigationState
import com.nevratov.matur.presentation.getApplicationComponent

@Composable
fun MainLoginScreen() {
    val navigationState = rememberNavigationState()
    val component = getApplicationComponent()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LoginNavGraph(
                navHostController = navigationState.navHostController,
                loginScreenContent = {
                    val viewModel: LoginViewModel = viewModel(factory = component.getViewModelFactory())
                    LoginScreen(viewModel = viewModel)
                },
            )
        }
    }
}