package com.nevratov.matur.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class NavigationState(
    val navHostController: NavHostController
) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(Screen.ChatList.route)
            launchSingleTop = true
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberAnimatedNavController()
): NavigationState = remember { NavigationState(navHostController) }