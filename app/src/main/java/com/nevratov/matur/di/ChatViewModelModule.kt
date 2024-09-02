package com.nevratov.matur.di

import androidx.lifecycle.ViewModel
import com.nevratov.matur.presentation.chat.ChatViewModel
import com.nevratov.matur.presentation.chat_list.ChatListViewModel
import com.nevratov.matur.presentation.login.LoginViewModel
import com.nevratov.matur.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChatViewModelModule {

    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    @Binds
    fun bindChatViewModel(impl: ChatViewModel): ViewModel
}