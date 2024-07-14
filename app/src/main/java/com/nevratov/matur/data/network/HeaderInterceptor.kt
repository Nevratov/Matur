package com.nevratov.matur.data.network

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .build()
        return chain.proceed(requestWithHeaders)
    }
}