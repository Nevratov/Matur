package com.nevratov.matur.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl("https://test.matur.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val productApi = retrofit.create(MaturApi::class.java)
}