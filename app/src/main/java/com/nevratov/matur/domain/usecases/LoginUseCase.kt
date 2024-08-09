package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(loginData: LoginData) = repository.login(loginData)
}