package com.nevratov.matur.presentation.matches

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nevratov.matur.R
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat_list.UserProfile

@Composable
fun MatchesScreen(
    users: List<User>,
    onMatchUserClicked: (User) -> Unit
) {

    //Test - to delete potom
    val testListMessageItems = mutableListOf<User>().apply {
        repeat(20) {
            add(
                User(
                    id = it,
                    name = "Kate $it",
                    logoUrl = "https://bipbap.ru/wp-content/uploads/2016/04/1566135836_devushka-v-shortah-na-pirone.jpg",
                    aboutMe = "",
                    gender = "",
                    birthday = "",
                    wasOnline = "",
                    cityId = 12,
                    height = 120,
                    weight = 120,
                    bodyType = "",
                    education = "",
                    job = "",
                    maritalStatus = "",
                    children = "",
                    house = "",
                    nationality = "",
                    languageSkills = "",
                    religion = "",
                    religiosityLevel = "",
                    expectations = "",
                    drinking = "",
                    smoking = ""
                )
            )
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.Matches_label),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            items(items = testListMessageItems, key = { it.id }) {
                MatchesItem(
                    user = it,
                    onItemClicked = onMatchUserClicked
                )
            }
        }
    }
}

@Composable
private fun MatchesItem(
    user: User,
    onItemClicked: (User) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .size(width = 200.dp, height = 320.dp)
            .clickable { onItemClicked(user) }
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(width = 200.dp, height = 250.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = user.logoUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

}