package com.nevratov.matur.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nevratov.matur.navigation.NavigationState

@Composable
fun BottomNavigationBar(
    navigationState: NavigationState
) {
    val navBacStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBacStackEntry?.destination?.route

    val items = listOf(
        NavigationItem.Explore,
        NavigationItem.Matches,
        NavigationItem.Chat,
        NavigationItem.Profile
    )

    NavigationBar {
        items.forEach { item ->
            val selectedItem = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selectedItem,
                onClick = {
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