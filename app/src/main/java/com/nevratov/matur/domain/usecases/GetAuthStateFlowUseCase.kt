package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.Repository

class GetAuthStateFlowUseCase(private val repository: Repository) {

    operator fun invoke() = repository.getAuthStateFlow()
}