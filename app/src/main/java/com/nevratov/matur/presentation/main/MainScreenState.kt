package com.nevratov.matur.presentation.main

sealed class MainScreenState {

    data object Initial : MainScreenState()

    data object Loading: MainScreenState()

    data object Login: MainScreenState()

    data object Registration: MainScreenState()

    data object Home: MainScreenState()
}