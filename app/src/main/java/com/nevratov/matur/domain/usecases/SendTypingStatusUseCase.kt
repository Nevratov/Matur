package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class SendTypingStatusUseCase @Inject constructor(
    private val repository: MaturRepository
) {

   suspend operator fun invoke(isTyping: Boolean, userId: Int, dialogUserId: Int) {
       repository.sendTypingStatus(isTyping, userId, dialogUserId)
   }
}