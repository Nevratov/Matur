package com.nevratov.matur.di

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.ViewModelFactory
import com.nevratov.matur.presentation.chat.ChatViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ChatViewModelModule::class])
interface ChatScreenComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance dialogUser: User): ChatScreenComponent
    }
}