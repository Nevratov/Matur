package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class GetMessagesByUserIdUseCase @Inject constructor(private val repository: MaturRepository) {

    operator fun invoke(id: Int) = repository.getChatByUserId(id)
}