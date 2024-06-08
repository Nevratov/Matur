package com.nevratov.matur.data

import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.Genders
import com.nevratov.matur.presentation.main.registration.RegUserInfo

class Mapper {

    fun userDtoToUser(userDto: UserDto): User {
        return User(
            id = userDto.id,
            name = userDto.name,
            email = userDto.email,
            gender = getGenderByNumber(userDto.gender),
            birthday = userDto.birthday,
            authKey = userDto.authKey,
            cityId = userDto.cityId,
            aboutMe = userDto.aboutMe,
            height = userDto.height,
            weight = userDto.height,
            bodyType = userDto.bodyType.toString(),
            education = userDto.education.toString(),
            job = userDto.job,
            maritalStatus = userDto.maritalStatus.toString(),
            children = userDto.children.toString(),
            house = userDto.house.toString(),
            nationality = userDto.nationality.toString(),
            languageSkills = userDto.languageSkills.toString(),
            religion = userDto.religion.toString(),
            religiosityLevel = userDto.religiosityLevel.toString(),
            expectations = userDto.expectations,
            drinking = userDto.drinking.toString(),
            smoking = userDto.smoking.toString()
        )
    }

    fun listUserDtoToListUser(listUserDto: List<UserDto>): List<User> {
        val users = mutableListOf<User>()
        listUserDto.forEach { users.add(userDtoToUser(it)) }
        return users
    }

    fun regUserInfoToRegUserInfoDto(regUserInfo: RegUserInfo): RegUserInfoDto {
        return RegUserInfoDto(
            name = regUserInfo.name,
            gender = getNumberByGender(regUserInfo.gender),
            email = regUserInfo.email,
            birthday = getBirthday(regUserInfo.day, regUserInfo.month, regUserInfo.year),
            cityId = regUserInfo.city?.id.toString()
        )
    }

    fun messageToMessageDto(message: Message): MessageDto {
        return MessageDto(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = message.content,
            timestamp = message.timestamp
        )
    }

    fun loginDataToLoginDataDto(loginData: LoginData): LoginDataDto {
        return LoginDataDto(
            email = loginData.email,
            password = loginData.password
        )
    }

    fun userToDislikedUserDto(user: User) = DislikedUserDto(userId = user.id)

    fun userToLikedUserDto(user: User) = LikedUserDto(userId = user.id)


    private fun getBirthday(day: String, month: String, year: String) = "$year-$month-$day"

    private fun getNumberByGender(nameGender: String): String {
        Genders.entries.forEach {
            if (it.genderName == nameGender) return it.number
        }
        throw RuntimeException("nameGender $nameGender not found in Genders")
    }

    private fun getGenderByNumber(number: Int): String {
        Genders.entries.forEach {
            if (it.number == number.toString()) return it.genderName
        }
        throw RuntimeException("number gender $number not found in Genders")
    }

}