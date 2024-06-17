package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.chat.Message
import javax.inject.Inject

class ReceiveMessageUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    operator fun invoke(message: Message) = repository.receiveMessage(message)
}