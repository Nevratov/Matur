package com.nevratov.matur.data

import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.model.ChatListItemDto
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.CreateNewFCMTokenDto
import com.nevratov.matur.data.model.RemoveMessageDto
import com.nevratov.matur.data.model.DislikedUserDto
import com.nevratov.matur.data.model.LikedUserDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RegUserInfoDto
import com.nevratov.matur.data.model.RemoveDialogDto
import com.nevratov.matur.data.model.WebSocketMessageDto
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.network.webSocket.WebSocketConst
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat_list.ChatListItem
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.Genders
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
            smoking = userDto.smoking.toString(),
            isBlocked = userDto.isBlocked
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
            replyMessage = message.replyMessage?.let { messageToMessageDto(it) }
        )
    }

    fun messageDtoToMessage(message: MessageDto) : Message {
        return Message(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = message.content,
            timestamp = message.timestampCreateSec * MILLIS_IN_SEC,
            timestampEdited = message.timestampUpdateSec * MILLIS_IN_SEC,
            isRead = message.isRead == 1,
            replyMessage = message.replyMessage?.let { messageDtoToMessage(message.replyMessage) }

        )
    }

    fun webSocketMessageDtoToMessage(webSocketMessage: WebSocketMessageDto): Message {
        val messageDto = Gson().fromJson(webSocketMessage.content, MessageDto::class.java)
        return messageDtoToMessage(messageDto)
    }

    fun messageToCreateMessageDto(message: Message): CreateMessageDto {
        return CreateMessageDto(
            receiverId = message.receiverId,
            messageJson = message.content,
            replyMessageId = message.replyMessage?.id
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

    fun webSocketMessageDtoToOnlineStatus(webSocketMessage: WebSocketMessageDto): OnlineStatus {
        val contentJson = Json.parseToJsonElement(webSocketMessage.content)
        val content = Json.encodeToString(contentJson)
        Log.d("webSocketTest", "encodeString = $content")
        return when (webSocketMessage.type) {
            WebSocketConst.STATUS_TYPE -> {
                OnlineStatus(
                    userId = webSocketMessage.senderId,
                    isOnline = content.contains(WebSocketConst.IS_ONLINE_STATUS)
                )
            }

            WebSocketConst.TYPING_TYPE -> {
                OnlineStatus(
                    userId = webSocketMessage.senderId,
                    isOnline = true,
                    isTyping = content.contains(WebSocketConst.IS_TYPING_CONTENT)
                )
            }

            else -> {
                throw RuntimeException("response type ${webSocketMessage.type} is unknown")
            }
        }
    }

    fun messageIdToDeleteMessageDto(id: Int): RemoveMessageDto = RemoveMessageDto(messageId = id)

    fun messageDtoToWebSocketMessageDto(message: MessageDto): WebSocketMessageDto {
        val messageJson = Gson().toJson(message)
        return WebSocketMessageDto(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = messageJson,
            timestamp = message.timestampCreateSec,
            type = WebSocketConst.MESSAGE_TYPE
        )
    }

    fun typingToWebSocketMessageDto(
        isTyping: Boolean,
        userId: Int,
        dialogUserId: Int
    ): WebSocketMessageDto {
        val content =
            if (isTyping) Gson().toJson(WebSocketConst.IS_TYPING_CONTENT)
            else Gson().toJson(WebSocketConst.IS_NOT_TYPING_CONTENT)

        return WebSocketMessageDto(
            id = 0,
            senderId = userId,
            receiverId = dialogUserId,
            content = content,
            timestamp = 0,
            type = WebSocketConst.TYPING_TYPE
        )
    }

    fun idToRemoveDialogDto(id: Int) = RemoveDialogDto(userId = id)

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