package com.nevratov.matur.navigation

sealed class Screen(val route: String) {

    data object Login: Screen(LOGIN_ROUTE)

    data object RegistrationContainer: Screen(REGISTRATION_ROUTE)

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
        fun getRouteWithArgs(dialogUserId: Int) = "$ROUTE_FOR_ARGS/${dialogUserId}"
    }

    data object Profile: Screen(PROFILE_ROUTE)

    companion object {
        const val KEY_DIALOG_USER_ID = "dialog_user_id"

        private const val LOGIN_ROUTE = "login"
        private const val REGISTRATION_ROUTE = "registration"
        private const val REQUEST_NAME_ROUTE = "request_name"
        private const val REQUEST_DATE_ROUTE = "request_date"
        private const val REQUEST_CITY_ROUTE = "request_city"
        private const val REQUEST_EMAIL_ROUTE = "request_email"
        private const val REGISTRATION_SUCCESS_ROUTE = "registration_success"
        private const val EXPLORE_ROUTE = "explore"
        private const val MATCHES_ROUTE = "matches"
        private const val CHAT_LIST_ROUTE = "chat_list"
        private const val CHAT_ROUTE = "chat/{$KEY_DIALOG_USER_ID}"
        private const val PROFILE_ROUTE = "profile"
    }
}

