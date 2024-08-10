package com.nevratov.matur.presentation.chat

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants.IterateForever
import com.airbnb.lottie.compose.rememberLottieComposition
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.MaturApplication
import com.nevratov.matur.ui.theme.GrayDarkColor_1
import com.nevratov.matur.ui.theme.GrayDarkColor_2
import com.nevratov.matur.ui.theme.GrayLightColor_2
import com.nevratov.matur.ui.theme.GrayLightColor_3
import com.nevratov.matur.ui.theme.PurpleColor_1
import com.nevratov.matur.ui.theme.PurpleColor_2

@Composable
fun ChatScreen(
    dialogUser: User,
    onBackPressed: () -> Unit
) {
    val component = (LocalContext.current.applicationContext as MaturApplication).component
    val viewModel = component.chatListComponentFactory().create(dialogUser).getViewModel()

    DisposableEffect(Unit) {
        onDispose { viewModel.onCleared() }
    }

    val screenState = viewModel.chatScreenState.collectAsState(initial = ChatScreenState.Initial)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val maxWidthItem = screenWidth * 0.85f

    val backgroundPattern =
        if (isSystemInDarkTheme()) R.drawable.pattern_dark_theme
        else R.drawable.pattern_light_theme

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .paint(
                    painter = painterResource(id = backgroundPattern),
                    contentScale = ContentScale.Crop
                )
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

        ChatScreenState.Initial -> {
            ShimmerListItem()
        }

        ChatScreenState.Loading -> {

        }
    }
}

@Composable
private fun Chat(
    screenState: ChatScreenState.Content,
    maxWidthItem: Dp,
    viewModel: ChatViewModel,
    onBackPressed: () -> Unit
) {
    val messageField = rememberSaveable(
        saver = textFieldSaver,
        init = { mutableStateOf(TextFieldValue("")) }
    )

    var messageMode by remember { mutableStateOf<MessageMode>(MessageMode.Classic) }
    val showEmojiPicker = remember { mutableStateOf(false) }

    var lastMessage by remember { mutableStateOf(screenState.messages.first()) }

    val lazyListState = rememberLazyListState()

    val groupMessagesByDate = screenState.messages.groupBy { it.date }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProfilePanel(
            screenState = screenState,
            viewModel = viewModel,
            onBackPressed = onBackPressed
        )

        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            item { Spacer(modifier = Modifier.width(6.dp)) }

            groupMessagesByDate.forEach { (date, messages) ->

                items(messages, key = { it.id }) { message ->

                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                messageMode = MessageMode.Reply(message)
                            }
                            false
                        },
                        positionalThreshold = { fullWith ->
                            fullWith * 0.1f
                        }
                    )
                    SwipeToDismissBox(
                        modifier = Modifier.animateItem(),
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            BackgroundDismissIco(dismissState = dismissState)
                        },
                        content = {
                            MessageItem(
                                message = message,
                                maxWidthItem = maxWidthItem,
                                screenState = screenState,
                                onEditClicked = {
                                    messageField.value =
                                        messageField.value.copy(text = message.content)
                                    messageMode = MessageMode.Edit(message)
                                },
                                onRemoveClicked = { viewModel.removeMessage(message) },
                                onReplyClicked = { messageMode = MessageMode.Reply(message) }
                            )
                        }
                    )
                }
                item(key = date) {
                    DateDelimiter(date = date)
                }
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

        ModificationMessageItem(
            messageMode = messageMode,
            onCloseModification = {
                messageField.value = TextFieldValue("")
                messageMode = MessageMode.Classic
            }
        )

        Typing(
            messageFieldState = messageField,
            onValueChanged = { newValue ->
                messageField.value = newValue.copy(text = newValue.text)
                viewModel.typing()
            },
            onEmojiIcoClicked = { showEmojiPicker.value = !showEmojiPicker.value },
            messageMode = messageMode,
            isBlockedUser = screenState.dialogUser.isBlocked,
            onConfirmClicked = {
                if (messageField.value.text.isBlank()) return@Typing
                when (val currentMessageMode = messageMode) {
                    is MessageMode.Edit -> {
                        viewModel.editMessage(
                            currentMessageMode.message.copy(
                                content = messageField.value.text,
                                timestampEdited = System.currentTimeMillis()
                            )
                        )
                    }

                    is MessageMode.Reply -> {
                        viewModel.sendMessage(
                            textMessage = messageField.value.text,
                            replyMessage = currentMessageMode.message
                        )
                    }

                    MessageMode.Classic -> {
                        viewModel.sendMessage(textMessage = messageField.value.text)
                    }
                }
                messageField.value = TextFieldValue("")
                messageMode = MessageMode.Classic
            }
        )

        ShowEmojiPicker(
            showEmojiPickerState = showEmojiPicker,
            onEmojiClicked = { emoji ->
                val newMessage = StringBuilder().append(messageField.value.text).append(emoji)
                messageField.value = messageField.value.copy(
                    text = newMessage.toString(),
                    selection = TextRange(newMessage.length)
                )
            }
        )
    }
}

private fun triggerVibrate(context: Context, durationMillis: Long = 50) {
    @Suppress("DEPRECATION")
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val vibrationEffect =
        VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(vibrationEffect)
}

@Composable
private fun DateDelimiter(
    date: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
                .padding(8.dp),
            color = Color.White,
            text = date,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BackgroundDismissIco(
    dismissState: SwipeToDismissBoxState
) {
    val isDismissed = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
    val sizeIco by animateDpAsState(targetValue = if (isDismissed) 26.dp else 18.dp, label = "size")

    val offset by animateDpAsState(targetValue = if (isDismissed) 0.dp else 30.dp, label = "offset")

    val context = LocalContext.current
    LaunchedEffect(key1 = isDismissed) {
        if (isDismissed) triggerVibrate(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset {
                IntOffset(
                    x = offset
                        .toPx()
                        .toInt(), y = 0
                )
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier.size(26.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(sizeIco),
                painter = painterResource(id = R.drawable.reply_ico),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@Composable
private fun ProfilePanel(
    screenState: ChatScreenState.Content,
    viewModel: ChatViewModel,
    onBackPressed: () -> Unit,
) {
    val isBlocked = screenState.dialogUser.isBlocked

    val profileUserActions = listOf(
        ProfileAction.Notification(isEnabled = true),
        ProfileAction.Search(),
        ProfileAction.RemoveDialog(),
        ProfileAction.Block(isBlocked = isBlocked)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White
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
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = screenState.dialogUser.name,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = Color.White
            )

            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = screenState.onlineStatus,
                transitionSpec = {
                    slideInVertically(tween(1000)) { it }.togetherWith(
                        slideOutVertically(tween(1000)) { -it }
                    )
                }, label = "animate status"
            ) { status ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val (color, textStatus) = if (status.isTyping) {
                        Pair(Color.Gray, stringResource(R.string.typing_status))
                    } else if (status.isOnline) {
                        Pair(Color.Green, stringResource(R.string.online_status))
                    } else {
                        Pair(Color.Red, screenState.dialogUser.wasOnlineText)
                    }

                    if (status.isTyping) {
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
        Row(
            Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ProfilePanelActions(actions = profileUserActions, viewModel = viewModel)
        }
    }
}

@Composable
private fun ProfilePanelActions(
    actions: List<ProfileAction>,
    viewModel: ChatViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var isShowWarningRemoveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    IconButton(
        onClick = { expanded = !expanded }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more_actions_button),
            tint = Color.White
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        actions.forEach { action ->
            DropdownMenuItem(
                text = { Text(text = stringResource(action.nameResId)) },
                onClick = {
                    when (action) {
                        is ProfileAction.Notification -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.waiting_server_implementation_toast),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ProfileAction.Search -> {
                            Toast.makeText(
                                context,
                                context.getString(R.string.waiting_server_implementation_toast),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ProfileAction.Block -> {
                            if (action.isBlocked) viewModel.unblockUser()
                            else viewModel.blockUser()
                        }

                        is ProfileAction.RemoveDialog -> {
                            isShowWarningRemoveDialog = true
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = action.icoResId),
                        contentDescription = stringResource(id = action.descriptionResId)
                    )
                }
            )
        }
    }
    if (isShowWarningRemoveDialog) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.remove_dialog_alert_title)) },
            text = {
                Text(text = stringResource(R.string.remove_dealog_alert_description))
            },
            onDismissRequest = { isShowWarningRemoveDialog = false },
            confirmButton = {
                Text(
                    modifier = Modifier.clickable { viewModel.removeDialog() },
                    text = stringResource(R.string.remove_confirm_button)
                )
            },
            dismissButton = {
                Text(
                    modifier = Modifier.clickable { isShowWarningRemoveDialog = false },
                    text = stringResource(R.string.remove_cancellation_button)
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

    Column(
        Modifier
            .height(200.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        EmojiPicker(onEmojiClicked = { onEmojiClicked(it) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Typing(
    messageFieldState: MutableState<TextFieldValue>,
    onConfirmClicked: () -> Unit,
    onValueChanged: (TextFieldValue) -> Unit,
    onEmojiIcoClicked: () -> Unit,
    messageMode: MessageMode,
    isBlockedUser: Boolean
) {
    val message = messageFieldState.value

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .weight(0.1f),
            onClick = { onEmojiIcoClicked() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.smile_ico),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
        }

        val interactionSource = remember { MutableInteractionSource() }
        Row(modifier = Modifier
            .weight(0.8f)
            .heightIn(min = 52.dp) // Default height parent Row
            .wrapContentHeight(),
           verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = Modifier.padding(vertical = 6.dp),
                enabled = !isBlockedUser,
                maxLines = 6,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                value = message,
                onValueChange = { onValueChanged(it) },
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = message.text,
                    innerTextField = innerTextField,
                    enabled = !isBlockedUser,
                    singleLine = false,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledIndicatorColor = MaterialTheme.colorScheme.background,
                    ),
                    placeholder = {
                        AnimatedContent(
                            targetState = isBlockedUser,
                            transitionSpec = {
                                slideInVertically(tween(2000)) { it }.togetherWith(
                                    slideOutVertically(tween(2000)) { -it }
                                )
                            }, label = "Placeholder animation"
                        ) {
                            val text = if (it) {
                                stringResource(id = R.string.is_blocked_placeholder_chat)
                            } else {
                                stringResource(id = R.string.message_placeholder_chat)
                            }
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = text
                            )
                        }
                    },
                )
            }
        }

        val icoConfirm =
            if (messageMode is MessageMode.Edit) Icons.Filled.Done else Icons.AutoMirrored.Outlined.Send
        IconButton(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .weight(0.1f),
            colors = IconButtonDefaults.iconButtonColors(),
            onClick = {
                onConfirmClicked()
            }
        ) {
            Icon(
                imageVector = icoConfirm,
                contentDescription = "send message",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MessageItem(
    modifier: Modifier = Modifier,
    screenState: ChatScreenState.Content,
    message: Message,
    maxWidthItem: Dp,
    onEditClicked: () -> Unit,
    onRemoveClicked: () -> Unit,
    onReplyClicked: () -> Unit
) {
    val isMenuVisibleState = remember { mutableStateOf(false) }
    val pressOffsetState = remember { mutableStateOf(DpOffset.Zero) }
    val itemHeightState = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val context = LocalContext.current

    val contentAlignment =
        if (message.senderId == screenState.user.id) Alignment.CenterEnd
        else Alignment.CenterStart

    val (messageColor, messageBackground) =
        if (message.senderId == screenState.user.id) {
            listOf(Color.White, MaterialTheme.colorScheme.primary)
        } else {
            listOf(MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.tertiary)
        }

    val messageShape =
        if (message.senderId == screenState.user.id) {
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
                        triggerVibrate(context = context, durationMillis = 20)
                        pressOffsetState.value = DpOffset(it.x.toDp(), it.y.toDp())
                        isMenuVisibleState.value = true
                    }
                )
            },
        contentAlignment = contentAlignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxWidthItem)
                .clip(messageShape)
                .background(messageBackground)
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            message.replyMessage?.let { replyMessage ->
                ReplyMessageItem(
                    message = message,
                    replyMessage = replyMessage,
                    screenState = screenState
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.weight(1f, fill = false)

                ) {
                    Text(
                        lineHeight = 20.sp,
                        text = message.content,
                        color = messageColor
                    )
                }
                TimeIsReadIsEdited(message = message, userId = screenState.user.id)
            }
        }

        val messageMenuItems = listOf(
            MessageActionItem.Edit(onEditClicked = onEditClicked),
            MessageActionItem.Remove(onRemoveClicked = onRemoveClicked),
            MessageActionItem.Reply(onReplyClicked = onReplyClicked)
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
private fun ReplyMessageItem(
    message: Message,
    replyMessage: Message,
    screenState: ChatScreenState.Content
) {
    val nameUser: String
    val backgroundColor: Color
    val markColor: Color
    val textColor: Color

    val isDark = isSystemInDarkTheme()
    //todo
    if (message.senderId == screenState.user.id) {
        nameUser = screenState.user.name
        backgroundColor = PurpleColor_1
        markColor = PurpleColor_2
        textColor = Color.White
    } else {
        nameUser = screenState.dialogUser.name
        backgroundColor = if (isDark) GrayDarkColor_1 else GrayLightColor_2
        markColor = if (isDark) GrayDarkColor_2 else GrayLightColor_3
        textColor = MaterialTheme.colorScheme.onBackground
    }

    Row(
        Modifier
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
    ) {
        Box(
            Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(markColor)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = nameUser,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = replyMessage.content,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ModificationMessageItem(
    messageMode: MessageMode,
    onCloseModification: () -> Unit
) {

    val modeIco: ImageVector
    val modeName: String
    val modeIcoDescription: String
    val textMessage: String

    when (messageMode) {
        MessageMode.Classic -> {
            return
        }

        is MessageMode.Edit -> {
            modeIco = Icons.Default.Edit
            modeName = stringResource(id = R.string.edit_message_action)
            modeIcoDescription = stringResource(id = R.string.edit_message_action)
            textMessage = messageMode.message.content
        }

        is MessageMode.Reply -> {
            modeIco = Icons.AutoMirrored.Filled.ArrowBack
            modeName = stringResource(id = R.string.reply_message_action)
            modeIcoDescription = stringResource(id = R.string.reply_message_description)
            textMessage = messageMode.message.content
        }
    }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .weight(0.1f),
                imageVector = modeIco,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = modeIcoDescription
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier.weight(0.8f)
            ) {
                Text(
                    text = modeName,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle.Default
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = textMessage,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle.Default
                )
            }

            IconButton(
                modifier = Modifier
                    .weight(0.1f),
                onClick = { onCloseModification() }
            ) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.modification_message_cancellation_button),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
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
private fun TimeIsReadIsEdited(
    message: Message,
    userId: Int
) {
    val icoId = if (message.isRead) R.drawable.check_mark_double else R.drawable.check_mark
    val timeColor = if (message.senderId == userId) Color.LightGray else Color.Gray

    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        if (message.timestamp != message.timestampEdited) {
            Text(
                text = stringResource(R.string.message_changed),
                fontSize = 10.sp,
                color = timeColor,
                style = TextStyle.Default
            )
        }
        Text(
            text = message.time,
            fontSize = 10.sp,
            color = timeColor,
            style = TextStyle.Default
        )
        if (userId == message.senderId) {
            Icon(
                modifier = Modifier
                    .size(width = 20.dp, height = 10.dp)
                    .padding(start = 4.dp, bottom = 2.dp),
                painter = painterResource(id = icoId),
                contentDescription = null
            )
        }
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
val textFieldSaver: Saver<MutableState<TextFieldValue>, String> = Saver(
    save = { it.value.text },
    restore = { mutableStateOf(TextFieldValue(text = it)) }
)
