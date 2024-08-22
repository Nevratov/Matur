package com.nevratov.matur.di

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.presentation.chat.ChatViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface ChatScreenComponent {

    fun getViewModel(): ChatViewModel

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance dialogUser: User): ChatScreenComponent
    }
}