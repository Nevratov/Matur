package com.nevratov.matur.presentation.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nevratov.matur.R
import com.nevratov.matur.ui.theme.MaturTheme

@Composable
fun MessagesScreen() {

    val testListMessageItems = mutableListOf<UserProfile>().apply {
        repeat(20) {
            add(UserProfile(
                id = it,
                firstName = "Kate",
                lastName = "Jhonson $it",
                logoUri = "https://bipbap.ru/wp-content/uploads/2016/04/1566135836_devushka-v-shortah-na-pirone.jpg",
                lastMessage = "I love you baby, go to sex on next night? I really like to vizit you!"

            ))
        }
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item() {
            Text(text = stringResource(R.string.messages_label), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }
       items(items = testListMessageItems, key = { it.id }) {userProfile ->
           MessageItem(userMessageProfile = userProfile)
       }
    }

}

@Composable
fun MessageItem(
    userMessageProfile: UserProfile
) {
    
    Row(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .clickable {  }
        .wrapContentHeight()
        .padding(8.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        AsyncImage(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
            ,
            model = userMessageProfile.logoUri,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            Text(
                text = "${userMessageProfile.firstName} ${userMessageProfile.lastName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = userMessageProfile.lastMessage,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMessageItem() {
    MaturTheme(darkTheme = false) {
        MessagesScreen()
    }
}