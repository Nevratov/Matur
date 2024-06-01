package com.nevratov.matur.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nevratov.matur.ui.theme.MaturColorDark
import com.nevratov.matur.ui.theme.MaturTheme


@Composable
fun ExploreCard() {
    val uriState by remember {
        mutableStateOf("https://bipbap.ru/wp-content/uploads/2016/04/1566135836_devushka-v-shortah-na-pirone.jpg")
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
            ,
            model = uriState,
            contentScale = ContentScale.Crop,
            contentDescription = "person's photo"
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Kate Jhonson",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Full-time Traveller. Globe Trotter. Occasional Photographer. Prt time Singer / Dancer",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ActionButton(ico = Icons.Rounded.Favorite, colorIco = MaturColorDark, onClick = {})
            Spacer(modifier = Modifier.width(64.dp))
            ActionButton(ico = Icons.Rounded.Close, onClick = {})
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
    IconButton(
        modifier = Modifier
            .size(60.dp)
            .shadow(elevation = 12.dp, shape = CircleShape),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = ico,
            contentDescription = "like person",
            tint = colorIco
        )
    }
}


@Preview
@Composable
private fun Preview() {
    MaturTheme(darkTheme = false) {
        ExploreCard()
    }
}