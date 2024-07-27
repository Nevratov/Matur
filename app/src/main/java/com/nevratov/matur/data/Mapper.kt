package com.nevratov.matur.data

import android.util.Log
import com.nevratov.matur.data.model.ChatListItemDto
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.CreateNewFCMTokenDto
import com.nevratov.matur.data.model.DeleteMessageDto
import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.ResponseWSDto
import com.nevratov.matur.data.model.SendMessageWSDto
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.network.webSocket.WebSocketConst
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.Genders
import com.nevratov.matur.presentation.main.registration.RegUserInfo

class Mapper {

    fun userDtoToUser(userDto: UserDto): User {
        Log.d("User", userDto.toString())
        return User(
            id = userDto.id,
            name = userDto.name,
            logoUrl = userDto.images.firstOrNull()?.sizes?.urlSquare1024,
            gender = getGenderByNumber(userDto.gender),
            birthday = userDto.birthday,
            wasOnlineTimestamp = userDto.wasOnlineTimestampSec * MILLIS_IN_SEC,
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
            isRead = if (message.isRead) 1 else 0,
            replyId = message.replyId
        )
    }

    fun messageDtoToMessage(message: MessageDto): Message {
        return Message(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = message.content,
            timestamp = message.timestampCreateSec * MILLIS_IN_SEC,
            timestampEdited = message.timestampUpdateSec * MILLIS_IN_SEC,
            isRead = message.isRead == 1,
            replyId = message.replyId
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
        timestamp = responseWSDto.timestamp * MILLIS_IN_SEC,
        timestampEdited = responseWSDto.timestamp * MILLIS_IN_SEC,
        isRead = false,
        replyId = null //todo
    )

    fun responseWSDtoToOnlineStatus(responseWSDto: ResponseWSDto): OnlineStatus {
        return when (responseWSDto.type) {
            WebSocketConst.STATUS_TYPE -> {
                OnlineStatus (
                    userId = responseWSDto.senderId,
                    isOnline = responseWSDto.content == WebSocketConst.IS_ONLINE
                )
            }
            WebSocketConst.TYPING_TYPE -> {
                OnlineStatus (
                    userId = responseWSDto.senderId,
                    isOnline = true,
                    isTyping = responseWSDto.content == WebSocketConst.IS_TYPING
                )
            }
            else -> { throw RuntimeException("response type ${responseWSDto.type} is unknown") }
        }
    }

    fun messageIdToDeleteMessageDto(id: Int): DeleteMessageDto = DeleteMessageDto(messageId = id)

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

    fun stringToCreateFCMTokenDto(newToken: String) = CreateNewFCMTokenDto(token = newToken)


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
    }
}