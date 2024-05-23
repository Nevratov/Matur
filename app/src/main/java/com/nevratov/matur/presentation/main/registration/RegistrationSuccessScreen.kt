package com.nevratov.matur.presentation.main.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun RegistrationSuccessScreen() {
    
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("animation_success.json")
    )
    
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(composition = composition)
    }
}