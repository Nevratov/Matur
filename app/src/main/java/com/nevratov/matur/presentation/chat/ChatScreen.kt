package com.nevratov.matur.presentation.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.nevratov.matur.data.network.webSocket.WebSocketClient
import com.nevratov.matur.data.network.webSocket.WebSocketListener


@Composable
fun ChatScreen() {
    val messages = remember { mutableStateListOf<String>() }
    val webSocketClient = remember { WebSocketClient("wss://test.matur.app/ws") }

    LaunchedEffect(webSocketClient) {
        webSocketClient.connect(listener = WebSocketListener { message ->
            messages.add(message)
        })
    }

    Column {
        ChatMessages(messages = messages)
        ChatForm(onSendMessage = { message ->
            webSocketClient.send(message)
        })
    }
}

@Composable
fun ChatMessages(messages: List<String>) {
    LazyColumn {
        item { 
            Text(text = "Test text")
        }
        items(messages) { message ->
            Text(text = message)
        }
    }
}

@Composable
fun ChatForm(
    onSendMessage: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }
    TextField(
        value = message,
        onValueChange = { message = it },
        label = { Text("Message") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(onSend = {
            onSendMessage(message)
            message = ""
        })
    )
}

@Preview
@Composable
private fun PreviewChatScreen() {
    ChatScreen()
}


