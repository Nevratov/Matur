package com.nevratov.matur.data.network.webSocket

import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.data.Mapper
import com.nevratov.matur.data.model.ReceivedMessageDto
import com.nevratov.matur.presentation.chat.Message
import com.nevratov.matur.presentation.chat.UserId
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener (
    private val onMessageReceived: (Message) -> Unit
): WebSocketListener() {

    private val mapper = Mapper()

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        outOut("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        outOut("Error: ${t.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val receivedMessageDto = Gson().fromJson(text, ReceivedMessageDto::class.java)
        val message = mapper.receivedMessageDtoToMessage(receivedMessageDto)
        onMessageReceived(message)
        outOut("Received: $text")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        val gson = Gson()
        val json = gson.toJson(UserId(sender_id = 1, receiver_id = 4))
        webSocket.send(json)
        outOut("webSocket onOpen - connected")
        outOut(json)

//        Log.d("webSocketTest", "webSocket onOpen - send $json")
    }

    private fun outOut(text: String) {
        Log.d("webSocketTest", text)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}