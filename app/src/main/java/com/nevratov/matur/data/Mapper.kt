package com.nevratov.matur.data

import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.model.ChatListItemDto
import com.nevratov.matur.data.model.CreateMessageDto
import com.nevratov.matur.data.model.CreateNewFCMTokenDto
import com.nevratov.matur.data.model.EditMessageDto
import com.nevratov.matur.data.model.LoginDataDto
import com.nevratov.matur.data.model.MessageDto
import com.nevratov.matur.data.model.MessagesOptionsDto
import com.nevratov.matur.data.model.RemoveDialogDto
import com.nevratov.matur.data.model.RemoveMessagesDto
import com.nevratov.matur.data.model.UserDto
import com.nevratov.matur.data.model.WebSocketMessageDto
import com.nevratov.matur.data.network.webSocket.WebSocketConst
import com.nevratov.matur.domain.entity.ChatListItem
import com.nevratov.matur.domain.entity.LoginData
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.domain.entity.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun userDtoToUser(userDto: UserDto): User {
        return User(
            id = userDto.id,
            name = userDto.name,
            logoUrl = userDto.images.firstOrNull()?.sizes?.urlSquare1024,
            gender = userDto.gender.toString(),
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
            isBlocked = userDto.isBlocked,
            hasBlocked = userDto.hasBlocked
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

    fun webSocketMessageDtoToRemoveMessagesId(webSocketMessage: WebSocketMessageDto): List<Int> {
        val removeMessages =
            Gson().fromJson(webSocketMessage.content, RemoveMessagesDto::class.java)
        return removeMessages.messageId
    }

    fun webSocketMessageDtoToOnlineStatus(webSocketMessage: WebSocketMessageDto): OnlineStatus {
        val contentJson = Json.parseToJsonElement(webSocketMessage.content)
        val content = Json.encodeToString(contentJson)
        return when (webSocketMessage.type) {
            WebSocketConst.STATUS_TYPE -> {
                OnlineStatus(
                    userId = webSocketMessage.senderId,
                    isOnline = content.contains(WebSocketConst.IS_ONLINE_CONTENT)
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

    fun messageToEditMessageDto(message: Message) = EditMessageDto(
        messageId = message.id,
        content = message.content
    )

    fun messagesIdToRemoveMessageDto(id: List<Int>, removeEveryone: Boolean) = RemoveMessagesDto(
        messageId = id,
        removeEveryone = removeEveryone
    )

    fun messageDtoToWebSocketMessageDto(message: MessageDto, uuid: String): WebSocketMessageDto {
        val messageJson = Gson().toJson(message)
        return WebSocketMessageDto(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = messageJson,
            timestamp = message.timestampCreateSec,
            type = WebSocketConst.MESSAGE_TYPE,
            uuid = uuid
        )
    }

    fun removeMessagesIdToWebSocketMessageDto(
        messagesId: List<Int>,
        removeEveryone: Boolean,
        senderId: Int,
        receiverId: Int,
        uuid: String
    ): WebSocketMessageDto {
        val removeMessages =
            messagesIdToRemoveMessageDto(id = messagesId, removeEveryone = removeEveryone)
        val messageJson = Gson().toJson(removeMessages)
        return WebSocketMessageDto(
            senderId = senderId,
            receiverId = receiverId,
            content = messageJson,
            type = WebSocketConst.DELETE_TYPE,
            uuid = uuid
        )
    }

    fun editMessageToWebSocketMessageDto(
        message: Message,
        senderId: Int,
        receiverId: Int,
        uuid: String
    ): WebSocketMessageDto {
        val messageDto = messageToMessageDto(message)
        val messageJson = Gson().toJson(messageDto)
        return WebSocketMessageDto(
            senderId = senderId,
            receiverId = receiverId,
            content = messageJson,
            type = WebSocketConst.EDIT_TYPE,
            uuid = uuid
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

    fun readMessageToWebSocketMessageDto(userId: Int, dialogUserId: Int): WebSocketMessageDto {
        return WebSocketMessageDto(
            senderId = userId,
            receiverId = dialogUserId,
            type = WebSocketConst.READ_ALL_TYPE,
            id = 0,
            timestamp = 0,
            content = "",
        )
    }

    fun idToRemoveDialogDto(id: Int, removeEveryone: Boolean) = RemoveDialogDto(
        userId = id,
        removeEveryone = if (removeEveryone) 1 else 0
    )

    fun loginDataToLoginDataDto(loginData: LoginData): LoginDataDto {
        return LoginDataDto(
            email = loginData.email,
            password = loginData.password
        )
    }

    fun toMessagesOptionsDto(messagesWithUserId: Int, page: Int) = MessagesOptionsDto(
        messagesWithUserId = messagesWithUserId.toString(),
        pageSize = (150 * page).toString(),
        page = "1"
    )

    fun tokenToCreateFCMTokenDto(newToken: String) = CreateNewFCMTokenDto(token = newToken)

    private fun messageToMessageDto(message: Message): MessageDto {
        return MessageDto(
            id = message.id,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = message.content,
            timestampCreateSec = message.timestamp / MILLIS_IN_SEC,
            timestampUpdateSec = message.timestampEdited / MILLIS_IN_SEC,
            isRead = if (message.isRead) 1 else 0,
            replyMessage = message.replyMessage?.let { messageToMessageDto(it) }
        )
    }


    private companion object {
        private const val MILLIS_IN_SEC = 1000
    }
}