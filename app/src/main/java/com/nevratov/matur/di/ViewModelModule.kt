package com.nevratov.matur.di

import androidx.lifecycle.ViewModel
import com.nevratov.matur.presentation.main.MainViewModel
import com.nevratov.matur.presentation.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    @Binds
    fun bindLoginViewModel(impl: LoginViewModel): ViewModel
}