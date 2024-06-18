package com.nevratov.matur.di

import android.app.Application
import com.nevratov.matur.presentation.ViewModelFactory
import com.nevratov.matur.presentation.chat.ChatViewModel
import com.nevratov.matur.presentation.main.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun chatListComponentFactory(): ChatListComponent.Factory

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}