package com.nevratov.matur.data.network.webSocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class WebSocketClient(
    private val url: String
) {

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect(listener: WebSocketListener) {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, listener)
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }

    fun send(message: String) {
        webSocket?.send(message)
    }
}