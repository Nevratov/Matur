package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.chat.Message
import javax.inject.Inject

class RemoveMessageUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(message: Message) = repository.removeMessage(message)
}