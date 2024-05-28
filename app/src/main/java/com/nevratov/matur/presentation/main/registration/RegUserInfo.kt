package com.nevratov.matur.presentation.main.registration

data class RegUserInfo(
    var name: String = EMPTY,
    var day: String = EMPTY,
    var month: String = EMPTY,
    var year: String = EMPTY,
    var gender: String = EMPTY,
    var email: String = EMPTY,
    var city: City? = null
) {
    companion object {
        private const val EMPTY = ""
    }
}
