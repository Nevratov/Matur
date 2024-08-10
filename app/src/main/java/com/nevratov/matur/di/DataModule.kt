package com.nevratov.matur.di

import com.nevratov.matur.data.network.ApiFactory
import com.nevratov.matur.data.network.ApiService
import com.nevratov.matur.data.repository.MaturRepositoryImpl
import com.nevratov.matur.domain.repoository.MaturRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindRepository(impl: MaturRepositoryImpl): MaturRepository

    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}