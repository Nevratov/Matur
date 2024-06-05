package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.Repository

class CheckAuthUseCase(private val repository: Repository) {

    operator fun invoke() = repository.checkAuthState()
}