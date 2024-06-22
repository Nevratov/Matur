package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class LoadNextMessagesUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(messagesWithId: Int) = repository.loadNextMessages(messagesWithId)
}