package com.nevratov.matur.presentation.chat

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.nevratov.matur.ui.theme.MaturAlternativeColor
import kotlin.random.Random

@Composable
fun ShimmerListItem() {
    Column {
        ShimmerProfilePanel()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(MAX_ITEMS_PER_SCREEN) {
                val isUserMessage = Random.nextBoolean()
                val widthWeight = Random.nextInt(3, 8) / 10f
                val height = Random.nextInt(30, 70).dp

                ShimmerMessageItem(
                    isUserMessage = isUserMessage,
                    widthWeight = widthWeight,
                    height = height
                )

            }
        }
    }
}

@Composable
private fun ShimmerProfilePanel() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaturAlternativeColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        IconButton(
            onClick = {  }
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .shimmerEffect(),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 18.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 12.dp)
                    .shimmerEffect(),
            )
        }
        Row(
            Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {  }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Больше действий",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ShimmerMessageItem(
    isUserMessage: Boolean,
    widthWeight: Float,
    height: Dp
) {
    val contentAlignment = if (isUserMessage) Alignment.CenterEnd else Alignment.CenterStart

    val messageShape = if (isUserMessage) {
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
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = contentAlignment,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = widthWeight)
                .clip(messageShape)
                .height(height)
                .shimmerEffect(),
        )
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffset by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = "shimmer effect"
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffset, 0f),
            end = Offset(startOffset + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

private const val MAX_ITEMS_PER_SCREEN = 14