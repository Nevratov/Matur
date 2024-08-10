package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke() = repository.checkAuthState()
}