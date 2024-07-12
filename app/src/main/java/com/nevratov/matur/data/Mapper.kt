package com.nevratov.matur.data

import android.icu.util.Calendar
import android.util.Log
import com.nevratov.matur.data.model.ChatListItemDto
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.ResponseWSDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.SendMessageWSDto
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.network.webSocket.WebSocketConst
import com.nevratov.matur.domain.entity.NetworkStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.Genders
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class Mapper {

    fun userDtoToUser(userDto: UserDto): User {
        Log.d("User", userDto.toString())
        return User(
            id = userDto.id,
            name = userDto.name,
            logoUrl = userDto.images.firstOrNull()?.sizes?.urlSquare1024,
            gender = getGenderByNumber(userDto.gender),
            birthday = userDto.birthday,
            wasOnline = timestampToTime(userDto.wasOnlineTimestampSec),
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
            timestampCreateSec = message.timestamp / 1000,
            timestampUpdateSec = message.timestamp / 1000,
            isRead = if (message.isRead) 1 else 0
        )
    }

    fun messageDtoToMessage(message: MessageDto): Message {
        return Message(
            id = message.id.toInt(),
            senderId = message.senderId.toInt(),
            receiverId = message.receiverId,
            content = message.content,
            timestamp = message.timestampCreateSec * 1000,
            isRead = message.isRead == 1
        )
    }

    fun messageToCreateMessageDto(message: Message): CreateMessageDto {
        return CreateMessageDto(
            receiverId = message.receiverId,
            message = message.content
        )
    }

    fun chatListDtoToChatList(chatListDto: List<ChatListItemDto>): List<ChatListItem> {
        val chatList = mutableListOf<ChatListItem>()
        chatListDto.forEach { chatListItem ->
            chatList.add(
                ChatListItem(
                    message = messageDtoToMessage(chatListItem.message),
                    user = userDtoToUser(chatListItem.user)
                )
            )
        }
        return chatList
    }

    fun responseWSDtoToMessage(responseWSDto: ResponseWSDto): Message = Message(
        id = responseWSDto.id,
        senderId = responseWSDto.senderId,
        receiverId = responseWSDto.receiverId,
        content = responseWSDto.content,
        timestamp = responseWSDto.timestamp * 1000,
        isRead = false
    )

    fun responseWSDtoToNetworkStatus(responseWSDto: ResponseWSDto): NetworkStatus = NetworkStatus (
        userId = responseWSDto.senderId,
        isOnline = responseWSDto.content == WebSocketConst.IS_ONLINE
    )

    // Now, we get MessageDTO in ResponseSendMessage, this method no actual
//    fun messageToSendMessageDto(message: Message): SendMessageWSDto {
//        return SendMessageWSDto(
//            senderId = message.senderId,
//            receiverId = message.receiverId,
//            message = message.content,
//
//        )
//    }

    fun messageDtoToSendMessageWSDto(message: MessageDto): SendMessageWSDto = SendMessageWSDto(
        senderId = message.senderId,
        receiverId = message.receiverId,
        message = message.content,
        id = message.id,
        timestamp = message.timestampCreateSec
    )

    fun loginDataToLoginDataDto(loginData: LoginData): LoginDataDto {
        return LoginDataDto(
            email = loginData.email,
            password = loginData.password
        )
    }

    fun userToDislikedUserDto(user: User) = DislikedUserDto(userId = user.id)

    fun userToLikedUserDto(user: User) = LikedUserDto(userId = user.id)


    fun toMessagesOptionsDto(messagesWithUserId: Int, page: Int) = MessagesOptionsDto(
        messagesWithUserId = messagesWithUserId.toString(),
        pageSize = (100 * page).toString(),
        page = "1"
    )

    private fun timestampToTime(timestampSec: Long): String {
        val currentTime = System.currentTimeMillis()
        val difference = currentTime - (timestampSec * MILLIS_IN_SEC)
        Log.d("timestampToTime", "current millis = $currentTime")
        Log.d("timestampToTime", "userWas millis = ${timestampSec * MILLIS_IN_SEC}")
        Log.d("timestampToTime", "$difference")

        val calendar = Calendar.getInstance().apply {
            time = Date(timestampSec * MILLIS_IN_SEC)
        }

        val minute = calendar.get(Calendar.MINUTE)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val pattern = "Был(а) в сети"
        return when(difference) {
            in 0..MILLIS_IN_HOUR -> {
                "$pattern ${TimeUnit.MILLISECONDS.toMinutes(difference)} мин. назад"
            }
            in MILLIS_IN_HOUR ..MILLIS_IN_6_HOUR -> {
                "$pattern ${TimeUnit.MILLISECONDS.toHours(difference)} ч. назад"
            }
            in MILLIS_IN_6_HOUR .. MILLIS_IN_DAY -> {
                "$pattern в $hour:$minute"
            }
            in MILLIS_IN_DAY .. MILLIS_IN_WEEK-> {
                String.format(Locale.getDefault(),"$pattern %02d.%02d в $hour:$minute", day, month)
            }
            in MILLIS_IN_WEEK .. MILLIS_IN_MONTH -> {
                "$pattern ${TimeUnit.MILLISECONDS.toDays(difference)} дн. назад"
            }
            else -> {
                String.format(Locale.getDefault(),"$pattern %02d.%02d.$year", day, month)
            }
        }
    }

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

    private companion object {
        private const val MILLIS_IN_SEC = 1000
        private const val MILLIS_IN_HOUR = 3_600_000
        private const val MILLIS_IN_6_HOUR = 21_600_000
        private const val MILLIS_IN_DAY = 86_400_000
        private const val MILLIS_IN_WEEK = 604_800_000
        private const val MILLIS_IN_MONTH = 2_592_000_000
    }
}