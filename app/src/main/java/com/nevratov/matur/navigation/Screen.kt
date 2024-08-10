package com.nevratov.matur.navigation

import android.net.Uri
import com.google.gson.Gson
import com.nevratov.matur.domain.entity.User

sealed class Screen(val route: String) {

    data object Login: Screen(LOGIN_ROUTE)

    data object ChatList: Screen(CHAT_LIST_ROUTE)

    data object Chat: Screen(CHAT_ROUTE) {
        private const val ROUTE_FOR_ARGS = "chat"
        fun getRouteWithArgs(dialogUser: User): String {
            val userJson = Gson().toJson(dialogUser)
            return "$ROUTE_FOR_ARGS/${userJson.encode()}"
        }
    }

    companion object {
        const val KEY_DIALOG_USER = "dialog_user"

        private const val LOGIN_ROUTE = "login"
        private const val CHAT_LIST_ROUTE = "chat_list"
        private const val CHAT_ROUTE = "chat/{$KEY_DIALOG_USER}"
    }
}

fun String.encode(): String = Uri.encode(this)

