package com.nevratov.matur.presentation.main.registration

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nevratov.matur.R
import com.nevratov.matur.navigation.AppNavGraph
import com.nevratov.matur.navigation.Screen
import com.nevratov.matur.navigation.rememberNavigationState


@Composable
fun RegistrationScreen() {
    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Log.d("RegistrationScreen", currentRoute.toString())
    Scaffold { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            requestNameScreenContent = { RequestNameScreen(
                onNextOnClickListener = { navigationState.navigateTo(Screen.RequestDate.route) },
                paddingValues = paddingValues
            ) },
            requestDateScreenContent = { RequestDateScreen(
                onNextClickListener = { navigationState.navigateTo(Screen.RequestEmail.route) },
                paddingValues = paddingValues
            ) },
            requestEmailScreenContent = { RequestEmailScreen(
                onNextClickListener = {

                },
                paddingValues = paddingValues
            ) },

        )
    }
}