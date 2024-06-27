package com.nevratov.matur.presentation.chat

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.emoji2.text.EmojiCompat
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
    Log.d(
        "Chat",
        "loadMessages = ${screenState.loadNextMessages}, isNextMessages = ${screenState.isNextMessages}"
    )
    val lazyListState = rememberLazyListState()
    var lastMessage by remember {
        mutableStateOf(screenState.messages.first())
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
            items(screenState.messages, key = { it.id }) { message ->
                Log.d("LazyColumnId", "message = ${message.content}, messageId = ${message.id}")
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
            Log.d("Chat", "lastMessage = $lastMessage")
            if (lastMessage != screenState.messages.first()) {
                lazyListState.scrollToItem(FIRST_ELEMENT)
            }
            lastMessage = screenState.messages.first()
            Log.d("Chat", "lastMessage = $lastMessage")

        }

        var message by remember {
            mutableStateOf("")
        }

        fun getMessage(): Message {
            val result = Message(
                id = 0,
                senderId = screenState.userId,
                receiverId = screenState.receiverId,
                content = message,
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            message = ""
            return result
        }


        var showEmojiPicker by remember {
            mutableStateOf(false)
        }

        if (showEmojiPicker) {

            EmojiPicker2 {
                message += it
                showEmojiPicker = false
            }

//            EmojiPicker { emoji ->
//                message += emoji
//                showEmojiPicker = false
//            }
        }



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
                        if (message.isEmpty()) return@KeyboardActions
                        viewModel.sendMessage(message = getMessage())
                    }
                ),
                value = message,
                onValueChange = { message = it },
            )

            IconButton(
                onClick = { showEmojiPicker = !showEmojiPicker }
            ) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = null)
            }

            IconButton(
                colors = IconButtonDefaults.iconButtonColors(),
                onClick = { viewModel.sendMessage(getMessage()) }
            ) {
                Icon(imageVector = Icons.Filled.Send, contentDescription = "send message")
            }
        }

    }
}

@Composable
private fun EmojiPicker2(
    onEmClicked: (String) -> Unit
) {

    Column {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                EmojiPickerView(context).apply {
                    emojiGridColumns = 9
                    emojiGridRows = 6f

                    setOnEmojiPickedListener { onEmClicked(it.emoji) }
                }
            },
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

//@Composable
//private fun EmojiPicker(
//    onEmojiClicked: (String) -> Unit
//) {
//    val emojis = listOf("😊", "😂", "😍", "😢", "😎", "😡", "👍", "🙏", "🎉", "❤️")
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.DarkGray)
//            .padding(8.dp)
//    ) {
//        LazyVerticalGrid(columns = GridCells.Adaptive(40.dp)) {
//             items(items = emojis) { emoji ->
//                 Text(
//                     modifier = Modifier
//                         .clickable { onEmojiClicked(emoji) },
//                     text = emoji,
//                     fontSize = 28.sp
//                 )
//             }
//        }
//    }
//}

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
