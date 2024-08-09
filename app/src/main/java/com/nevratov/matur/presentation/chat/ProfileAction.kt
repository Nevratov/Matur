package com.nevratov.matur.presentation.chat

import com.nevratov.matur.R

sealed class ProfileAction(
    val icoResId: Int,
    val nameResId: Int,
    val descriptionResId: Int
) {

    class Notification(
        isEnabled: Boolean,
        nameResId: Int = if (isEnabled) R.string.disable_notifications_action else R.string.enable_notifications_action,
        icoResId: Int = if (isEnabled) R.drawable.enable_notification_ico else R.drawable.disable_notifications_ico
    ): ProfileAction(
        icoResId = icoResId,
        nameResId = nameResId,
        descriptionResId = nameResId
    )

    class Search(
        nameResId: Int = R.string.search_action
    ): ProfileAction(
        icoResId = R.drawable.search_ico,
        nameResId = nameResId,
        descriptionResId = nameResId
    )

    class RemoveDialog(
        nameResId: Int = R.string.remove_dialog_action
    ): ProfileAction(
        icoResId = R.drawable.remove_chat_ico2,
        nameResId = nameResId,
        descriptionResId = nameResId
    )

    class Block(
        val isBlocked: Boolean,
        nameResId: Int = if (isBlocked) R.string.unblock_action else R.string.block_action,
        icoResId: Int = R.drawable.block_user_ico
    ): ProfileAction(
        icoResId = icoResId,
        nameResId = nameResId,
        descriptionResId = nameResId
    )
}