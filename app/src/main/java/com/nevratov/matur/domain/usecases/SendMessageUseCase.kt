package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.domain.entity.Message
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(message: Message) = repository.sendMessage(message)
}