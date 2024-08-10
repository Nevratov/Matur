package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import javax.inject.Inject

class LikeUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    suspend operator fun invoke(likedUser: User) = repository.like(likedUser)
}