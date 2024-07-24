package com.nevratov.matur.presentation.chat

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDismissState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.MaturApplication
import com.nevratov.matur.ui.theme.MaturAlternativeColor
import com.nevratov.matur.ui.theme.VeryLightGray

@Composable
fun ChatScreen(
    dialogUser: User,
    onBackPressed: () -> Unit
) {
    Log.d("Rebugger", "ChatScreen")
    val component = (LocalContext.current.applicationContext as MaturApplication).component
    val viewModel = component.chatListComponentFactory().create(dialogUser).getViewModel()

    DisposableEffect(Unit) {
        onDispose { viewModel.onCleared() }
    }

    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidthItem = screenWidth * 0.70f


    Scaffold() { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            ChatScreenContent(
                screenState = screenState,
                maxWidthItem = maxWidthItem,
                viewModel = viewModel,
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
private fun ChatScreenContent(
    screenState: State<ChatScreenState>,
    maxWidthItem: Dp,
    viewModel: ChatViewModel,
    onBackPressed: () -> Unit
) {
    when (val currentState = screenState.value) {
        is ChatScreenState.Content -> {
            Chat(
                screenState = currentState,
                maxWidthItem = maxWidthItem,
                viewModel = viewModel,
                onBackPressed = onBackPressed
            )
        }

        ChatScreenState.Initial -> {}

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Chat(
    screenState: ChatScreenState.Content,
    maxWidthItem: Dp,
    viewModel: ChatViewModel,
    onBackPressed: () -> Unit
) {
    val inputMessage = remember { mutableStateOf(TextFieldValue("")) }
    var messageEditing by remember { mutableStateOf<Message?>(null) }
    val showEmojiPicker = remember { mutableStateOf(false) }

    var lastMessage by remember { mutableStateOf(screenState.messages.first()) }

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfilePanel(screenState = screenState, onBackPressed = onBackPressed)

        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom =  6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
//            item { SeparateLine() }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(screenState.messages, key = { it.id }) { message ->
                val dismissState = rememberDismissState()
                val directions =
                    if (message.senderId == screenState.userId) setOf(DismissDirection.EndToStart) else setOf()
                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    directions = directions,
                    background = { },
                    dismissContent = {
                        MessageItem(
                            message = message,
                            maxWidthItem = maxWidthItem,
                            userId = screenState.userId,
                            onEditClicked = {
                                inputMessage.value = inputMessage.value.copy(text = message.content)
                                messageEditing = message
                            },
                            onRemoveClicked = { viewModel.removeMessage(message) }
                        )
                    }
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

        messageEditing?.let { message ->
            EditingMessageItem(
                message = message,
                onCloseEditing = {
                    inputMessage.value = TextFieldValue("")
                    messageEditing = null
                }
            )
        }

        Typing(
            messageState = inputMessage,
            onValueChanged = { newValue ->
                inputMessage.value = newValue.copy(text = newValue.text)
            },
            onEmojiIcoClicked = { showEmojiPicker.value = !showEmojiPicker.value },
            messageEditing = messageEditing,
            onConfirmClicked = {
                if (inputMessage.value.text.isBlank()) return@Typing
                val currentMessageEditing = messageEditing
                if (currentMessageEditing != null) {
                    viewModel.editMessage(currentMessageEditing.copy(content = inputMessage.value.text))
                    messageEditing = null
                } else {
                    viewModel.sendMessage(inputMessage.value.text)
                }
                inputMessage.value = TextFieldValue("")
            }
        )

        ShowEmojiPicker(
            showEmojiPickerState = showEmojiPicker,
            onEmojiClicked = { emoji ->
                val newMessage = StringBuilder().append(inputMessage.value.text).append(emoji)
                inputMessage.value = inputMessage.value.copy(
                    text = newMessage.toString(),
                    selection = TextRange(newMessage.length)
                )
            }
        )
    }
}

@Composable
private fun SeparateLine() {
    Spacer(modifier = Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .padding(horizontal = 12.dp)
            .background(VeryLightGray)
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun ProfilePanel(
    screenState: ChatScreenState.Content,
    onBackPressed: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaturAlternativeColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.background
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape),
            model = screenState.dialogUser.logoUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = screenState.dialogUser.name,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.background
            )

            val (color, textStatus) = if (screenState.onlineStatus.isTyping) {
                Pair(Color.Gray, "печатает")
            } else if (screenState.onlineStatus.isOnline) {
                Pair(Color.Green, "online")
            } else {
                Pair(Color.Red, screenState.dialogUser.wasOnlineText)
            }

            Row(verticalAlignment = Alignment.Bottom) {
                if (screenState.onlineStatus.isTyping) {
                    TypingAnimation()
                } else {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = textStatus,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                )
            }
        }
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
    messageState: MutableState<TextFieldValue>,
    onConfirmClicked: () -> Unit,
    onValueChanged: (TextFieldValue) -> Unit,
    onEmojiIcoClicked: () -> Unit,
    messageEditing: Message? = null,
) {
    val message = messageState.value

    Row(
        modifier = Modifier.padding(start = 12.dp, end = 6.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .weight(1f)
                .background(VeryLightGray),
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                modifier = Modifier.padding(bottom = 4.dp),
                onClick = { onEmojiIcoClicked() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.frame_13__1_),
                    contentDescription = null
                )
            }

            TextField(
                modifier = Modifier
                    .weight(1f)
                    .background(VeryLightGray),
                placeholder = { Text(stringResource(R.string.message_placeholer_chat)) },
                maxLines = 6,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = VeryLightGray,
                    unfocusedIndicatorColor = VeryLightGray,
                ),
                value = message,
                onValueChange = { onValueChanged(it) },
            )
        }

        val icoConfirm = if (messageEditing == null) Icons.Filled.Send else Icons.Filled.Done
        IconButton(
            modifier = Modifier.padding(bottom = 4.dp),
            colors = IconButtonDefaults.iconButtonColors(),
            onClick = {
                onConfirmClicked()
            }
        ) {
            Icon(imageVector = icoConfirm, contentDescription = "send message")
        }
    }
}

@Composable
private fun MessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    maxWidthItem: Dp,
    userId: Int,
    onEditClicked: (Message) -> Unit,
    onRemoveClicked: (Message) -> Unit
) {
    val isMenuVisibleState = remember { mutableStateOf(false) }
    val pressOffsetState = remember { mutableStateOf(DpOffset.Zero) }
    val itemHeightState = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    val contentAlignment =
        if (message.senderId == userId) Alignment.CenterEnd
        else Alignment.CenterStart

    val (messageColor, messageBackground) =
        if (message.senderId == userId) {
            listOf(MaterialTheme.colorScheme.background, MaturAlternativeColor)
        } else {
            listOf(MaterialTheme.colorScheme.onBackground, VeryLightGray)
        }

    val messageShape =
        if (message.senderId == userId) {
            RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = 20.dp,
                bottomEnd = 0.dp
            )
        } else {
            RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 20.dp,
                bottomStart = 20.dp,
                bottomEnd = 20.dp
            )
        }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { itemHeightState.value = with(density) { it.height.toDp() } }
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        pressOffsetState.value = DpOffset(it.x.toDp(), it.y.toDp())
                        isMenuVisibleState.value = true
                        Log.d("onGloballyPositioned", it.toString())
                    }
                )
            },
        contentAlignment = contentAlignment
    ) {
        Row(
            modifier = Modifier
                .clip(messageShape)
                .background(messageBackground)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = maxWidthItem)
                    .wrapContentHeight()
            ) {
                Text(
                    lineHeight = 20.sp,
                    text = message.content,
                    color = messageColor
                )
            }
            MessageTimeAndIsRead(message = message, userId = userId)
        }

        val messageMenuItems = listOf(
            MessageActionItem.Edit(onEditClicked = { onEditClicked(message) }),
            MessageActionItem.Remove(onRemoveClicked = { onRemoveClicked(message) })
        )
        OnMessageClickedMenu(
            isMenuVisibleState = isMenuVisibleState,
            offsetState = pressOffsetState,
            onDismissed = { isMenuVisibleState.value = false },
            itemHeightState = itemHeightState,
            menuItems = messageMenuItems
        )
    }
}

@Composable
private fun EditingMessageItem(
    message: Message,
    onCloseEditing: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            modifier = Modifier
                .size(22.dp)
                .weight(0.1f)
            ,
            imageVector = Icons.Default.Edit,
            tint = MaturAlternativeColor,
            contentDescription = stringResource(id = R.string.edit_message_action)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(
            modifier = Modifier.weight(0.8f)
        ) {
            Text(
                text = "Редактирование",
                fontSize = 16.sp,
                color = MaturAlternativeColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = message.content,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
            ,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { onCloseEditing() }
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Отменить"
                )
            }
        }

    }
}

@Composable
private fun OnMessageClickedMenu(
    isMenuVisibleState: MutableState<Boolean>,
    offsetState: MutableState<DpOffset>,
    itemHeightState: MutableState<Dp>,
    onDismissed: () -> Unit,
    menuItems: List<MessageActionItem>
) {
    val offset = offsetState.value
    val visible = isMenuVisibleState.value
    val itemHeight = itemHeightState.value

    DropdownMenu(
        expanded = visible,
        onDismissRequest = { onDismissed() },
        offset = offset.copy(y = offset.y - itemHeight)
    ) {
        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = stringResource(id = item.titleResId)) },
                leadingIcon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.descriptionResId)
                    )
                },
                onClick = {
                    onDismissed()
                    item.action()
                }
            )
        }
    }
}

@Composable
private fun MessageTimeAndIsRead(
    message: Message,
    userId: Int
) {
    val icoId = if (message.isRead) R.drawable.check_mark_double else R.drawable.check_mark
    val timeColor = if (message.senderId == userId) Color.LightGray else Color.Gray
    Text(
        text = message.time,
        fontSize = 11.sp,
        color = timeColor
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

@Composable
private fun TypingAnimation() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("typing_animation.json")
    )
    LottieAnimation(
        modifier = Modifier.size(14.dp),
        composition = composition,
        speed = 2f,
        iterations = IterateForever
    )
}

private const val FIRST_ELEMENT = 0
