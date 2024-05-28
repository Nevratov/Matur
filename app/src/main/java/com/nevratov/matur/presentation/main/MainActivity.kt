package com.nevratov.matur.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.nevratov.matur.presentation.main.registration.RegistrationScreen
import com.nevratov.matur.presentation.main.registration.RegistrationSuccessScreen
import com.nevratov.matur.presentation.main.registration.RequestCityScreen
import com.nevratov.matur.presentation.message.ChatScreen
import com.nevratov.matur.ui.theme.MaturTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaturTheme {
//                RequestCityScreen(onCitySelected = {}, paddingValues = PaddingValues(8.dp))
                RegistrationScreen()
//                ChatScreen()
            }
        }
    }
}
