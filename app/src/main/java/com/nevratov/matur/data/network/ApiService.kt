package com.nevratov.matur.data.network

import com.nevratov.matur.data.network.dto.RegUserInfoDto
import com.nevratov.matur.presentation.main.registration.City
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/create")
    suspend fun registerUser(@Body regUserInfo: RegUserInfoDto)

    @GET("city/get-by-name")
    suspend fun getCitiesByName(@Query("q") name: String) : List<City>
}