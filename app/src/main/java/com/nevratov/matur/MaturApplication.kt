package com.nevratov.matur

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.nevratov.matur.di.ApplicationComponent
import com.nevratov.matur.di.DaggerApplicationComponent

class MaturApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as MaturApplication).component
}