package com.nevratov.matur.navigation

import android.net.Uri
import com.google.gson.Gson
import com.nevratov.matur.domain.entity.User

sealed class Screen(val route: String) {

    data object Login: Screen(LOGIN_ROUTE)

    data object RequestName: Screen(REQUEST_NAME_ROUTE)

    data object RequestDate: Screen(REQUEST_DATE_ROUTE)

    data object RequestCity: Screen(REQUEST_CITY_ROUTE)

    data object RequestEmail: Screen(REQUEST_EMAIL_ROUTE)

    data object RegistrationSuccess: Screen(REGISTRATION_SUCCESS_ROUTE)

    data object Explore: Screen(EXPLORE_ROUTE)

    data object Matches: Screen(MATCHES_ROUTE)

    data object ChatList: Screen(CHAT_LIST_ROUTE)

    data object Chat: Screen(CHAT_ROUTE) {
        private const val ROUTE_FOR_ARGS = "chat"
        fun getRouteWithArgs(dialogUser: User): String {
            val userJson = Gson().toJson(dialogUser)
            return "$ROUTE_FOR_ARGS/${userJson.encode()}"
        }
    }

    data object Profile: Screen(PROFILE_ROUTE)

    companion object {
        const val KEY_DIALOG_USER = "dialog_user"

        private const val LOGIN_ROUTE = "login"
        private const val REQUEST_NAME_ROUTE = "request_name"
        private const val REQUEST_DATE_ROUTE = "request_date"
        private const val REQUEST_CITY_ROUTE = "request_city"
        private const val REQUEST_EMAIL_ROUTE = "request_email"
        private const val REGISTRATION_SUCCESS_ROUTE = "registration_success"
        private const val EXPLORE_ROUTE = "explore"
        private const val MATCHES_ROUTE = "matches"
        private const val CHAT_LIST_ROUTE = "chat_list"
        private const val CHAT_ROUTE = "chat/{$KEY_DIALOG_USER}"
        private const val PROFILE_ROUTE = "profile"
    }
}

fun String.encode(): String = Uri.encode(this)

