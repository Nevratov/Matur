package com.nevratov.matur.presentation.explore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.ui.theme.MaturColorDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel
) {
    val screenState = viewModel.state.collectAsState(initial = ExploreScreenState.Initial)

    ExploreScreenContent(
        screenState = screenState,
        viewModel = viewModel
    )
}

@Composable
fun ExploreScreenContent(
    screenState: State<ExploreScreenState>,
    viewModel: ExploreViewModel
) {
    when (val currentState = screenState.value) {
        is ExploreScreenState.Content ->
            ShowPostCard(
                user = currentState.exploreUser,
                viewModel = viewModel
            )

        ExploreScreenState.ContentIsEmpty -> {
            EmptyContentScreen()
        }

        ExploreScreenState.Initial -> {

        }

        ExploreScreenState.Loading -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPostCard(
    user: User,
    viewModel: ExploreViewModel
) {

    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        viewModel.like(user)
    } else if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        viewModel.dislike(user)
    }

//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        item(key = user.id) {
    SwipeToDismiss(
        state = dismissState,
        background = { },
        dismissContent = {
            ExploreCard(
                name = user.name,
                aboutMe = user.aboutMe,
                logoUrl = user.logoUrl,
                onLikeClicked = { viewModel.like(user) }
            )
        }
    )
//        }
//    }
}

@Composable
private fun EmptyContentScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Вы попытались познакомиться со всеми возможными людьми",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            fontFamily = FontFamily.Cursive,
            color = MaturColorDark
        )
    }
}


@Composable
private fun ExploreCard(
    name: String,
    aboutMe: String,
    logoUrl: String,
    onLikeClicked: () -> Unit
) {
    val uriState by remember {
        mutableStateOf(logoUrl)
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp)),
            model = uriState,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = name,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = aboutMe,
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionButton(ico = Icons.Rounded.Close, onClick = {})
            Spacer(modifier = Modifier.width(64.dp))
            ActionButton(
                ico = Icons.Rounded.Favorite,
                colorIco = MaturColorDark,
                onClick = { onLikeClicked() }
            )
        }

    }
}

@Composable
private fun ActionButton(
    ico: ImageVector,
    colorIco: Color = LocalContentColor.current,
    onClick: () -> Unit

) {
    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .shadow(elevation = 30.dp, shape = CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = ico,
            contentDescription = "like person",
            tint = colorIco
        )
    }
}
