package com.nevratov.matur.presentation.chat_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.ChatListItem
import com.nevratov.matur.domain.entity.Message
import com.nevratov.matur.presentation.chat.TypingAnimation
import com.nevratov.matur.presentation.getApplicationComponent

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
    onMessageItemClicked: (ChatListItem) -> Unit
) {
    val screenState = viewModel.state.collectAsState(initial = ChatListScreenState.Initial)
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            ChatListContent(
                state = screenState,
                onMessageItemClicked = onMessageItemClicked,
                userId = viewModel.getUser().id
            )
        }
    }
}

@Composable
private fun ChatListContent(
    state: State<ChatListScreenState>,
    onMessageItemClicked: (ChatListItem) -> Unit,
    userId: Int
) {
    when (val currentState = state.value) {
        is ChatListScreenState.Content -> {
            ChatList(
                chatList = currentState.chatList,
                onMessageItemClicked = onMessageItemClicked,
                userId = userId
            )
        }

        ChatListScreenState.Initial -> {

        }

        ChatListScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ChatList(
    chatList: List<ChatListItem>,
    onMessageItemClicked: (ChatListItem) -> Unit,
    userId: Int
) {

    Column {
        LazyColumn(
            modifier = Modifier.padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = chatList, key = { it.user.id }) { chatListItem ->
                MessageItem(
                    chatListItem = chatListItem,
                    onMessageItemClicked = onMessageItemClicked,
                    userId = userId
                )
                SeparateLine()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.messages_label),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White //todo
        )
    )
}

@Composable
fun MessageItem(
    chatListItem: ChatListItem,
    onMessageItemClicked: (ChatListItem) -> Unit,
    userId: Int
) {

    Row(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .height(IntrinsicSize.Min)
        .clickable { onMessageItemClicked(chatListItem) }
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape),
            model = chatListItem.user.logoUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(0.9f)) {
            Text(
                text = chatListItem.user.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (chatListItem.isTyping) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TypingAnimation()
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.typing_status),
                        fontSize = 12.sp,
                        color = Color.Gray,
                    )
                }
            } else {
                Text(
                    text = chatListItem.message.content,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageTimeAndIsRead(message = chatListItem.message, userId = userId)

                NewMessageIco(message = chatListItem.message, userId = userId)
            }
        }
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
            .background(Color.Gray.copy(alpha = 0.1f))
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun MessageTimeAndIsRead(
    message: Message,
    userId: Int
) {
    val icoId = if (message.isRead) R.drawable.check_mark_double else R.drawable.check_mark

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userId == message.senderId) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 4.dp),
                painter = painterResource(id = icoId),
                contentDescription = null,
                tint = Color.Gray
            )
        }
        Text(
            text = message.time,
            fontSize = 11.sp,
            color = Color.Gray,
            style = TextStyle.Default
        )
    }
}

@Composable
private fun ColumnScope.NewMessageIco(
    message: Message,
    userId: Int
) {
    if (!message.isRead && message.senderId != userId) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
            )
        }
    }
}



