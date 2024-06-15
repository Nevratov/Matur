package com.nevratov.matur.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.Beige
import com.nevratov.matur.ui.theme.MaturTheme


@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {

    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidthItem = screenWidth * 0.85f

    ChatScreenContent(
        screenState = screenState,
        maxWidthItem = maxWidthItem,
        viewModel = viewModel
    )

}

@Composable
private fun ChatScreenContent(
    screenState: State<ChatScreenState>,
    maxWidthItem: Dp,
    viewModel: ChatViewModel
) {
    when (val currentState = screenState.value) {
        is ChatScreenState.Content -> {
            Chat(
                messages = currentState.messages,
                maxWidthItem = maxWidthItem,
                userId = currentState.userId,
                onSend = { viewModel.sendMessage(it) }
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
    messages: List<Message>,
    maxWidthItem: Dp,
    userId: Int,
    onSend: (Message) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                MessageItem(
                    message = message,
                    maxWidthItem = maxWidthItem,
                    userId = userId
                )
            }
        }

        var message by remember {
            mutableStateOf("")
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.message_placeholer_chat)) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    message = ""
                    onSend(Message(
                        id = 0,
                        senderId = userId,
                        receiverId = "2",
                        content = message,
                        timestamp = System.currentTimeMillis(),
                        isRead = false
                    )
                    )
                }
            ),
            value = message,
            onValueChange = { message = it },
        )
    }


}

@Composable
private fun MessageItem(
    message: Message,
    maxWidthItem: Dp,
    userId: Int
) {

    val contentAlignment = if (message.senderId == userId) Alignment.CenterEnd
    else Alignment.CenterStart

    val messageBackground = if (message.senderId == userId) Beige else Color.White

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(messageBackground)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = maxWidthItem)
                    .wrapContentHeight()
            ) {
                Text(
                    lineHeight = 20.sp,
                    text = message.content
                )
            }
            Text(
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

    val testListMessages = mutableListOf<Message>().apply {
        add(
            Message(
                id = 3, senderId = 2, receiverId = "4",
                content = "Привет, как дела?  Что делаешь? Пойдём гулять вечером?",
                timestamp = 1718208768, isRead = false,
            ),
        )
        add(
            Message(
                id = 4, senderId = 4, receiverId = "2",
                content = "Да, ок",
                timestamp = 1718318799, isRead = false,
            ),
        )
        add(
            Message(
                id = 5, senderId = 4, receiverId = "2",
                content = "А то у меня дела есть некоторые",
                timestamp = 1718528822, isRead = false,
            ),
        )
        add(
            Message(
                id = 6, senderId = 2, receiverId = "4",
                content = "Понял, хорошо, зайду тогда за тобой к восьми, до встречи, и не опаздывай, как всегда!",
                timestamp = 1718748822, isRead = false,
            ),
        )
        //
        add(
            Message(
                id = 7, senderId = 2, receiverId = "4",
                content = "Привет, как дела?  Что делаешь? Пойдём гулять вечером?",
                timestamp = 1718208768, isRead = false,
            ),
        )
        add(
            Message(
                id = 8, senderId = 4, receiverId = "2",
                content = "Привет, всё хорошо.  Да, давай, только после 20.00",
                timestamp = 1718318799, isRead = false,
            ),
        )
        add(
            Message(
                id = 9, senderId = 4, receiverId = "2",
                content = "А то у меня дела есть некоторые",
                timestamp = 1718528822, isRead = false,
            ),
        )
        add(
            Message(
                id = 10, senderId = 2, receiverId = "4",
                content = "Понял, хорошо",
                timestamp = 1718748822, isRead = false,
            ),
        )
        //
        add(
            Message(
                id = 11, senderId = 2, receiverId = "4",
                content = "Привет, как дела?  Что делаешь? Пойдём гулять вечером?",
                timestamp = 1718208768, isRead = false,
            ),
        )
        add(
            Message(
                id = 12, senderId = 4, receiverId = "2",
                content = "Давай, только после 18.30",
                timestamp = 1718318799, isRead = false,
            ),
        )
        add(
            Message(
                id = 13, senderId = 4, receiverId = "2",
                content = "А то у меня дела есть некоторые",
                timestamp = 1718528822, isRead = false,
            ),
        )
        add(
            Message(
                id = 14, senderId = 2, receiverId = "4",
                content = "Ладно",
                timestamp = 1718748822, isRead = false,
            ),
        )
        //
        add(
            Message(
                id = 15, senderId = 2, receiverId = "4",
                content = "Привет, как дела?  Что делаешь? Пойдём гулять вечером?",
                timestamp = 1718208768, isRead = false,
            ),
        )
        add(
            Message(
                id = 16, senderId = 4, receiverId = "2",
                content = "Привет, всё хорошо.  Да, давай, только после 20.00",
                timestamp = 1718318799, isRead = false,
            ),
        )
        add(
            Message(
                id = 17, senderId = 4, receiverId = "2",
                content = "А то у меня дела есть некоторые",
                timestamp = 1718528822, isRead = false,
            ),
        )
        add(
            Message(
                id = 18, senderId = 2, receiverId = "4",
                content = "Как скажешь!",
                timestamp = 1718748822, isRead = false,
            ),
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MaturTheme(darkTheme = false) {
            Chat(
                messages = testListMessages,
                maxWidthItem = 300.05002.dp,
                userId = 2,
                onSend = {}
            )
        }
    }
}