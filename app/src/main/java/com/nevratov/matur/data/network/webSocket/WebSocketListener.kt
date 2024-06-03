package com.nevratov.matur.data.network.webSocket

import android.util.Log
import com.google.gson.Gson
import com.nevratov.matur.presentation.chat.UserId
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class WebSocketListener(
    private val onMessageReceived: (String) -> Unit
): WebSocketListener() {

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        outOut("Closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        outOut("Error: ${t.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageReceived(text)
        outOut("Received: $text")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        val gson = Gson()
        val json = gson.toJson(UserId(4))
//        webSocket.send(json)
        Log.d("webSocketTest", "webSocket onOpen - connected")
//        Log.d("webSocketTest", "webSocket onOpen - send $json")
    }

    private fun outOut(text: String) {
        Log.d("webSocketTest", text)
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}