package com.nevratov.matur.presentation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.provider.FontRequest
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.nevratov.matur.di.ApplicationComponent
import com.nevratov.matur.di.DaggerApplicationComponent

class MaturApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    return (LocalContext.current.applicationContext as MaturApplication).component
}