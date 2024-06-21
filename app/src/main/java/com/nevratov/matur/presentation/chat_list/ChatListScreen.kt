package com.nevratov.matur.presentation.chat_list

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nevratov.matur.R
import com.nevratov.matur.presentation.chat.Message

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
    onMessageItemClicked: (ChatListItem) -> Unit
) {

    val screenState = viewModel.state.collectAsState(initial = ChatListScreenState.Initial)

    ChatListContent(
        state = screenState,
        onMessageItemClicked = onMessageItemClicked,
        userId = viewModel.getUser().id
    )
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
                modifier = Modifier.fillMaxSize(),
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

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.messages_label),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(items = chatList, key = { it.user.id }) { chatListItem ->
            MessageItem(
                chatListItem = chatListItem,
                onMessageItemClicked = onMessageItemClicked,
                userId = userId
            )
        }
    }

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
        .clickable { onMessageItemClicked(chatListItem) }
        .wrapContentHeight()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Log.d("MessageItem", "id = ${chatListItem.user.id}, url = ${chatListItem.user.logoUrl}")


        AsyncImage(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape),
            model = chatListItem.user.logoUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            Text(
                text = chatListItem.user.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = chatListItem.message.content,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            MessageTimeAndIsRead(message = chatListItem.message, userId = userId)
        }
    }
}

@Composable
private fun MessageTimeAndIsRead(
    message: Message,
    userId: Int
) {
    val icoId = if (message.isRead) R.drawable.check_mark_double else R.drawable.check_mark
    if (userId == message.senderId) {
        Icon(
            modifier = Modifier.size(22.dp).padding(end = 4.dp),
            painter = painterResource(id = icoId),
            contentDescription = null
        )
    }
    Text(
        text = message.time,
        fontSize = 11.sp,
        color = Color.Gray
    )
}



