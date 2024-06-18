package com.nevratov.matur.di

import com.nevratov.matur.presentation.chat.ChatViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent()
interface ChatListComponent {

    fun getViewModel(): ChatViewModel

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance receiverId: Int): ChatListComponent
    }
}