package com.nevratov.matur.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateTo(route: String) {
        navHostController.navigate(route)
    }

    fun navigateToRegistrationSuccess(route: String) {
        navHostController.navigate(route) {
            popUpTo(0)
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState = remember { NavigationState(navHostController) }