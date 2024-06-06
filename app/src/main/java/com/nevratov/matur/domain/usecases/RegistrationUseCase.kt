package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import com.nevratov.matur.presentation.main.registration.City
import com.nevratov.matur.presentation.main.registration.RegUserInfo
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(regUserInfo: RegUserInfo) = repository.registration(regUserInfo)
}