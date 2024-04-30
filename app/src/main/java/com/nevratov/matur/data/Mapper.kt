package com.nevratov.matur.data

import com.nevratov.matur.data.network.dto.RegUserInfoDto
import com.nevratov.matur.presentation.main.registration.Genders
import com.nevratov.matur.presentation.main.registration.RegUserInfo

class Mapper {

    fun RegUserInfoToRegUserInfoDto(regUserInfo: RegUserInfo): RegUserInfoDto {
        return RegUserInfoDto(
            name = regUserInfo.name,
            gender = getNumberGender(regUserInfo.gender),
            email = regUserInfo.email,
            birthday = getBirthday(regUserInfo.day, regUserInfo.month, regUserInfo.year)
        )
    }

    private fun getBirthday(day: String, month: String, year: String) = "$year-$month-$day"

    private fun getNumberGender(nameGender: String): String {
        Genders.entries.forEach {
            if (it.genderName == nameGender) return it.number
        }
        throw RuntimeException("nameGender $nameGender not found in Genders")
    }
}