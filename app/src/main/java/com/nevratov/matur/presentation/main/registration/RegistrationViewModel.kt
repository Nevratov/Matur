package com.nevratov.matur.presentation.main.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.entity.City
import com.nevratov.matur.domain.usecases.GetCitiesByNameUseCase
import com.nevratov.matur.domain.usecases.RegistrationUseCase
import com.nevratov.matur.presentation.main.registration.RegistrationState.Initial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(
    val getCitiesByNameUseCase: GetCitiesByNameUseCase,
    val registrationUseCase: RegistrationUseCase
): ViewModel() {

    private val userInfo = RegUserInfo.initial

    private var _regState = MutableStateFlow<RegistrationState>(Initial)
    val regState: StateFlow<RegistrationState> = _regState

    fun setName(name: String) {
        userInfo.name = name
    }

    fun setBirthdayAndGender(day: String, month: String, year: String, gender: String) {
        userInfo.day = String.format(Locale.getDefault(),"%02d", day.toInt())
        userInfo.month = getMonthNumber(month)
        userInfo.year = year
        userInfo.gender = gender
    }

    fun setEmail(email: String) {
        userInfo.email = email
        registration()
    }

    
    fun getCitiesByName(name: String) {
        viewModelScope.launch {
            val cities = getCitiesByNameUseCase(name)
            _regState.value = RegistrationState.RequestCity(cities = cities)
        }
    }

    fun setCity(city: City) {
        userInfo.city = city
    }

    private fun getMonthNumber(month: String): String {
        Months.entries.forEach {
            if (it.monthName == month) return it.number
        }
        throw RuntimeException("nameMonth $month not found in Months")
    }


    private fun registration() {
        viewModelScope.launch {
            registrationUseCase(userInfo)
        }
    }
}