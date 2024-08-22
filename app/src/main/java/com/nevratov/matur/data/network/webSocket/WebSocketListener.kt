package com.nevratov.matur.data.network.webSocket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.model.WebSocketMessageDto
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.OnlineStatus
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener (
    private val onMessageReceived: (Message) -> Unit,
    private val onStatusReceived: (OnlineStatus) -> Unit,
    private val onUserIdReadAllMessages: (Int) -> Unit,
    private val senderId: Int,
    private val uuid: String
): WebSocketListener() {

    private val mapper = Mapper()

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) { }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val responseDto = Gson().fromJson(text, WebSocketMessageDto::class.java)
        when(responseDto.type) {
            WebSocketConst.MESSAGE_TYPE -> {
                onMessageReceived(mapper.webSocketMessageDtoToMessage(responseDto))
            }
            WebSocketConst.STATUS_TYPE, WebSocketConst.TYPING_TYPE -> {
                onStatusReceived(mapper.webSocketMessageDtoToOnlineStatus(responseDto))
            }
            WebSocketConst.READ_ALL_TYPE -> {
                onUserIdReadAllMessages(responseDto.senderId)
            }
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val json = JsonObject().apply {
            addProperty("sender_id", senderId)
            addProperty("uuid", uuid)
        }
        val jsonString = Gson().toJson(json)
        webSocket.send(jsonString)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}