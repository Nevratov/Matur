package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.City
import javax.inject.Inject

class GetCitiesByNameUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(name: String): List<City> = repository.getCitiesByName(name)
}