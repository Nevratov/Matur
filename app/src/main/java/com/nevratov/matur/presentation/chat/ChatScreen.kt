package com.nevratov.matur.presentation.chat

import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.Beige
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel
) {
    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidthItem = screenWidth * 0.80f

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
                receiverId = currentState.receiverId,
                loadNextMessages = currentState.loadNextMessages,
                viewModel = viewModel
            )
            if (!currentState.isNextMessages) viewModel.showToast()
            Log.d("getMessagesByUserId", currentState.isNextMessages.toString())
        }

        ChatScreenState.Initial -> {

        }

        ChatScreenState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
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
    receiverId: Int,
    loadNextMessages: Boolean,
    viewModel: ChatViewModel
) {
    Log.d("Chat", "REC")
    val lazyListState = rememberLazyListState()
    var lastMessage by remember {
        mutableStateOf(messages.first())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            items(messages, key = { it.id }) { message ->
                Log.d("LazyColumnId", "message = ${message.content}, messageId = ${message.id}")
                MessageItem(
                    message = message,
                    maxWidthItem = maxWidthItem,
                    userId = userId
                )
            }
            item {
                if (loadNextMessages) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                } else {
                    SideEffect {
                        Log.d("messageId", "SideEffect")
                        viewModel.loadNextMessages()
                    }
                }
            }
        }



        LaunchedEffect(key1 = messages.size) {
                Log.d("Chat", "lastMessage = $lastMessage")
                if (lastMessage != messages.first()) {
                    lazyListState.scrollToItem(FIRST_ELEMENT)
                }
            lastMessage = messages.first()
            Log.d("Chat", "lastMessage = $lastMessage")

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
                    if (message.isEmpty()) return@KeyboardActions
                    viewModel.sendMessage( message =
                        Message(
                            id = 0,
                            senderId = userId,
                            receiverId = receiverId.toString(),
                            content = message,
                            timestamp = System.currentTimeMillis(),
                            isRead = false
                        )
                    )
                    message = ""
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
            MessageTimeAndIsRead(message = message, userId = userId)
        }
    }
}

@Composable
private fun MessageTimeAndIsRead(
    message: Message,
    userId: Int
) {
    val icoId = if (message.isRead) R.drawable.check_mark_double else R.drawable.check_mark
    Text(
        text = message.time,
        fontSize = 11.sp,
        color = Color.Gray
    )
    if (userId == message.senderId) {
        Icon(
            modifier = Modifier
                .size(18.dp)
                .padding(start = 4.dp),
            painter = painterResource(id = icoId),
            contentDescription = null
        )
    }
}

private const val FIRST_ELEMENT = 0
