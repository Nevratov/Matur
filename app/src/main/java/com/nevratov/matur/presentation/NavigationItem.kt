package com.nevratov.matur.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.nevratov.matur.R
import com.nevratov.matur.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val descriptionResId: Int,
    val icon: ImageVector,
) {

    data object Explore: NavigationItem(
        screen = Screen.Explore,
        titleResId = R.string.explore_navigation_item,
        descriptionResId = R.string.explore_ico_description,
        icon = Icons.Outlined.Search
    )
    data object Matches: NavigationItem(
        screen = Screen.Matches,
        titleResId = R.string.matches_navigation_item,
        descriptionResId = R.string.matches_description_ico,
        icon = Icons.Outlined.FavoriteBorder
    )
    data object Chat: NavigationItem(
        screen = Screen.Chat,
        titleResId = R.string.chat_navigation_item,
        descriptionResId = R.string.chat_description_ico,
        icon = Icons.Outlined.MailOutline
    )
    data object Profile: NavigationItem(
        screen = Screen.Profile,
        titleResId = R.string.profile_navigation_item,
        descriptionResId = R.string.profile_description_ico,
        icon = Icons.Outlined.Person
    )

}
