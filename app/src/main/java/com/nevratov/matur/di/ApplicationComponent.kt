package com.nevratov.matur.di

import android.app.Application
import com.nevratov.matur.presentation.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getChatScreenComponentFactory(): ChatScreenComponent.Factory

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}