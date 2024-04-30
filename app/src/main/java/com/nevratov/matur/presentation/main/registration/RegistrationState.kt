package com.nevratov.matur.presentation.main.registration

sealed class RegistrationState {

    data object Initial: RegistrationState()

    data class RequestNameState(
        val firstName: String,
        val lastName: String,
        val isErrorFirstName: Boolean,
        val isErrorLastName: Boolean
    ) : RegistrationState()

    data class RequestDateState(
        var day: String = EMPTY,
        var month: String = EMPTY,
        var year: String = EMPTY,
        var gender: String = EMPTY
    ) : RegistrationState()

    data class RequestEmailState(
        val email: String
    ) : RegistrationState()

    companion object {
        private const val EMPTY = ""
    }
}