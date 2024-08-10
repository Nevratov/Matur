package com.nevratov.matur.presentation.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.nevratov.matur.navigation.NavigationState
import com.nevratov.matur.presentation.BottomNavigationBar

@Composable
fun MatchesScreen(
    users: List<User>,
    navigationState: NavigationState,
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
                    wasOnlineTimestamp = 0,
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
                    smoking = "",
                    isBlocked = false
                )
            )
        }
    }

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNavigationBar(navigationState = navigationState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            MatchesContent(
                items = testListMessageItems,
                onMatchUserClicked = onMatchUserClicked
            )
        }
    }
}

@Composable
private fun MatchesContent(
    items: List<User>,
    onMatchUserClicked: (User) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {
        items(2) { /*top padding with correct scroll*/  }
        items(items = items, key = { it.id }) {
            MatchesItem(
                user = it,
                onItemClicked = onMatchUserClicked
            )
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
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.Matches_label),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        )
    )
}