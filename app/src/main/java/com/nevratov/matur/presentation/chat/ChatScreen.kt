package com.nevratov.matur.presentation.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevratov.matur.R
import com.nevratov.matur.di.ChatScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.MaturApplication
import com.nevratov.matur.ui.theme.Beige

@Composable
fun ChatScreen(
    dialogUser: User
) {
    Log.d("ChatScreen", "REC")

    val component = (LocalContext.current.applicationContext as MaturApplication).component
    val viewModel = component.chatListComponentFactory().create(dialogUser).getViewModel()

    DisposableEffect(Unit) {
        onDispose { viewModel.onCleared() }
    }

    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidthItem = screenWidth * 0.70f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatScreenContent(
            screenState = screenState,
            maxWidthItem = maxWidthItem,
            viewModel = viewModel
        )
    }

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
                screenState = currentState,
                maxWidthItem = maxWidthItem,
                viewModel = viewModel
            )
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
    screenState: ChatScreenState.Content,
    maxWidthItem: Dp,
    viewModel: ChatViewModel
) {
    Log.d("Chat", "REC")
    val message = remember { mutableStateOf(TextFieldValue("")) }
    val showEmojiPicker = remember { mutableStateOf(false) }

    var lastMessage by remember { mutableStateOf(screenState.messages.first()) }

    val lazyListState = rememberLazyListState()



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        
//        Row {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
//            }
//            // Photo
//            Column {
//                Text(text = )
//            }
//        }



        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            items(screenState.messages, key = { it.id }) { message ->
                MessageItem(
                    message = message,
                    maxWidthItem = maxWidthItem,
                    userId = screenState.userId
                )
            }
            // Load next messages
            item {
                if (screenState.loadNextMessages) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                } else if (!screenState.isNextMessages) {
                    viewModel.showToast()
                } else {
                    SideEffect {
                        Log.d("messageId", "SideEffect")
                        viewModel.loadNextMessages()
                    }
                }
            }
        }

        LaunchedEffect(key1 = screenState.messages.size) {
            if (lastMessage != screenState.messages.first()) {
                lazyListState.scrollToItem(FIRST_ELEMENT)
            }
            lastMessage = screenState.messages.first()

        }

        Typing(
            screenState = screenState,
            messageState = message,
            viewModel = viewModel,
            onValueChanged = { newValue ->
                message.value = newValue.copy(
                    text = newValue.text,
                    selection = TextRange(newValue.text.length)
                )
            },
            onEmojiIcoClicked = { showEmojiPicker.value = !showEmojiPicker.value },
            messageReset = { message.value = TextFieldValue("") }
        )

        ShowEmojiPicker(
            showEmojiPickerState = showEmojiPicker,
            onEmojiClicked = { emoji ->
                val newMessage = StringBuilder().append(message.value.text).append(emoji)
                message.value = message.value.copy(
                    text = newMessage.toString(),
                    selection = TextRange(newMessage.length)
                )
            }
        )
    }
}

@Composable
private fun ShowEmojiPicker(
    showEmojiPickerState: MutableState<Boolean>,
    onEmojiClicked: (String) -> Unit
) {
    val showEmojiPicker = showEmojiPickerState.value
    if (!showEmojiPicker) return

    Column(Modifier.height(200.dp)) {
        EmojiPicker(onEmojiClicked = { onEmojiClicked(it) })
    }
}

@Composable
private fun Typing(
    screenState: ChatScreenState.Content,
    messageState: MutableState<TextFieldValue>,
    viewModel: ChatViewModel,
    onValueChanged: (TextFieldValue) -> Unit,
    onEmojiIcoClicked: () -> Unit,
    messageReset: () -> Unit
) {
    val message = messageState.value

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            placeholder = { Text(text = stringResource(R.string.message_placeholer_chat)) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White

            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.text.isEmpty()) return@KeyboardActions
                    viewModel.sendMessage(message =
                        getMessage(
                            screenState = screenState,
                            messageState = messageState,
                            messageReset = messageReset
                        )
                    )
                }
            ),
            value = message,
            onValueChange = { onValueChanged(it) },
        )

        IconButton(
            onClick = { onEmojiIcoClicked() }
        ) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = null)
        }

        IconButton(
            colors = IconButtonDefaults.iconButtonColors(),
            onClick = {
                viewModel.sendMessage(message =
                    getMessage(
                        screenState = screenState,
                        messageState = messageState,
                        messageReset = messageReset
                    )
                )
            }
        ) {
            Icon(imageVector = Icons.Filled.Send, contentDescription = "send message")
        }
    }
}

fun getMessage(
    screenState: ChatScreenState.Content,
    messageState: MutableState<TextFieldValue>,
    messageReset: () -> Unit
): Message {
    val message = messageState.value

    val result = Message(
        id = 0,
        senderId = screenState.userId,
        receiverId = screenState.receiverId,
        content = message.text,
        timestamp = System.currentTimeMillis(),
        isRead = false
    )
    messageReset()
    return result
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
