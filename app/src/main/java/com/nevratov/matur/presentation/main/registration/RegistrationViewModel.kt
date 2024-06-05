package com.nevratov.matur.presentation.main.registration

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.repository.MaturRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.RuntimeException
import com.nevratov.matur.presentation.main.registration.RegistrationState.Initial
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class RegistrationViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MaturRepositoryImpl(application)

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
            val cities = repository.getCitiesByName(name = name)
            _regState.value = RegistrationState.RequestCity(cities = cities)
            Log.d("getCitiesByName", _regState.value.toString())
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
            repository.registration(userInfo)
            Log.d("RegistrationViewModel", userInfo.toString())
        }
    }
    
}