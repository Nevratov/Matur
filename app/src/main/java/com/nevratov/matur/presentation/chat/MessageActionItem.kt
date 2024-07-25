package com.nevratov.matur.presentation.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.nevratov.matur.R
import com.nevratov.matur.navigation.Screen

sealed class MessageActionItem(
    val titleResId: Int,
    val descriptionResId: Int,
    val icon: ImageVector,
    val action: () -> Unit
) {

    data class Edit(
        val onEditClicked: () -> Unit
    ): MessageActionItem(
        titleResId = R.string.edit_message_action,
        descriptionResId = R.string.edit_message_description,
        icon = Icons.Default.Edit,
        action = onEditClicked
    )
    data class Remove(
        val onRemoveClicked: () -> Unit
    ): MessageActionItem(
        titleResId = R.string.remove_message_action,
        descriptionResId = R.string.remove_message_description,
        icon = Icons.Default.Delete,
        action = onRemoveClicked
    )
}


