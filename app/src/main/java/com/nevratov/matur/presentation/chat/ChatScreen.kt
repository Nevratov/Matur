package com.nevratov.matur.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.ui.theme.MaturTheme


@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {

    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    ChatScreenContent(
        screenState = screenState
    )

}

@Composable
private fun ChatScreenContent(
    screenState: State<ChatScreenState>
) {
    when (val currentState = screenState.value) {
        is ChatScreenState.Content -> {
            Chat(
                messages = currentState.messages
            )
        }

        ChatScreenState.Initial -> {

        }

        ChatScreenState.Loading -> {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun Chat(
    messages: List<Message>
) {
    LazyColumn {
        items(messages) {

        }
    }
}

@Composable
private fun MessageItem(message: Message) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidth = screenWidth * 0.85f
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Magenta)
            ,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = maxWidth)
                    .wrapContentHeight()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(text = message.content)
            }
            Text(
                modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                text = message.time,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }


}

@Preview
@Composable
private fun MessageItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        MaturTheme(darkTheme = false) {
            MessageItem(
                message = Message(
                    id = "3", senderId = "2", receiverId = "4",
                    content = "Привет, как дела?  Что делаешь? VVV",
                    timestamp = 1718408768, isRead = false
                )
            )
        }
    }
}