package com.nevratov.matur.domain.usecases

import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.repoository.MaturRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetUsersToExploreUseCase @Inject constructor(
    private val repository: MaturRepository
) {

    operator fun invoke(): StateFlow<List<User>> = repository.getUsersToExplore()
}