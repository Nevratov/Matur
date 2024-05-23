package com.nevratov.matur.presentation.main.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.data.repository.RepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.RuntimeException
import com.nevratov.matur.presentation.main.registration.RegistrationState.Initial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Locale

class RegistrationViewModel: ViewModel() {
    private val repository = RepositoryImpl()
    private val userInfo = RegUserInfo()


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