package com.nevratov.matur.navigation

sealed class Screen(val route: String) {

    data object RegistrationContainer: Screen(REGISTRATION_ROUTE)

    data object RequestName : Screen(REQUEST_NAME_ROUTE)

    data object RequestDate : Screen(REQUEST_DATE_ROUTE)

    data object RequestEmail : Screen(REQUEST_EMAIL_ROUTE)

    data object RegistrationSuccess : Screen(REGISTRATION_SUCCESS_ROUTE)


    private companion object {
        private const val REGISTRATION_ROUTE = "registration"
        private const val REQUEST_NAME_ROUTE = "request_name"
        private const val REQUEST_DATE_ROUTE = "request_date"
        private const val REQUEST_EMAIL_ROUTE = "request_email"
        private const val REGISTRATION_SUCCESS_ROUTE = "registration_success"
    }
}

