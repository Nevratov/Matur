package com.nevratov.matur.di

import com.nevratov.matur.data.repository.MaturRepositoryImpl
import com.nevratov.matur.domain.repoository.MaturRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    abstract fun bindLocalDataSource(impl: MaturRepositoryImpl): MaturRepository
}