package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class ResetDialogOptionsUseCase @Inject constructor(private val repository: MaturRepository) {

    operator fun invoke() = repository.resetDialogOptions()
}