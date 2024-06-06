package com.nevratov.matur.presentation.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen() {


    Scaffold(
        bottomBar = {

        }
    ) {
 Text(text = it.toString())
    }
}