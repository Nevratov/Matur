package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class CreateNewFCMTokenUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    operator fun invoke(newToken: String) = repository.createNewFCMToken(newToken)
}