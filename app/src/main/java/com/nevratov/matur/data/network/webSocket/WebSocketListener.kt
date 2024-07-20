package com.nevratov.matur.data.network.webSocket

import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.model.ResponseWSDto
import com.nevratov.matur.domain.entity.OnlineStatus
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat.UserId
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener (
    private val onMessageReceived: (Message) -> Unit,
    private val onStatusReceived: (OnlineStatus) -> Unit,
    private val onUserIdReadAllMessages: (Int) -> Unit,
    private val senderId: Int
): WebSocketListener() {

    private val mapper = Mapper()

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        logWebSocket("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        logWebSocket("Error: ${t.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val responseDto = Gson().fromJson(text, ResponseWSDto::class.java)
        when(responseDto.type) {
            WebSocketConst.MESSAGE_TYPE -> {
                onMessageReceived(mapper.responseWSDtoToMessage(responseDto))
            }
            WebSocketConst.STATUS_TYPE, WebSocketConst.TYPING_TYPE -> {
                Log.d("chatScreenState", "type = $responseDto")
                onStatusReceived(mapper.responseWSDtoToOnlineStatus(responseDto))
            }
            WebSocketConst.READ_ALL_TYPE -> {
                onUserIdReadAllMessages(responseDto.senderId)
            }
        }
        logWebSocket("Received: $text")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val json = Gson().toJson(UserId(
            sender_id = senderId
        ))
        webSocket.send(json)
        logWebSocket("webSocket onOpen - connected")
        logWebSocket(json)
    }

    private fun logWebSocket(text: String) {
        Log.d("webSocketTest", text)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}