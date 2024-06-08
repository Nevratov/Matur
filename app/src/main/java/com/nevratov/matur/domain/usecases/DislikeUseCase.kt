package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import com.nevratov.matur.presentation.main.login.LoginData
import javax.inject.Inject

class DislikeUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(dislikedUser: User) = repository.dislike(dislikedUser)
}