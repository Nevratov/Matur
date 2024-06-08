package com.nevratov.matur.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.usecases.DislikeUseCase
import com.nevratov.matur.domain.usecases.GetUsersToExploreUseCase
import com.nevratov.matur.domain.usecases.LikeUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExploreViewModel @Inject constructor(
    getUsersToExploreUseCase: GetUsersToExploreUseCase,
    private val likeUseCase: LikeUseCase,
    private val dislikeUseCase: DislikeUseCase
): ViewModel() {

    val state = getUsersToExploreUseCase()
        .map { users ->
            if (users.isEmpty()) ExploreScreenState.ContentIsEmpty
            else ExploreScreenState.Content(users.first()) as ExploreScreenState
        }
        .onStart { ExploreScreenState.Initial }


    fun like(likedUser: User) {
        viewModelScope.launch {
            likeUseCase(likedUser)
        }
    }

    fun dislike(dislikedUser: User) {
        viewModelScope.launch {
            dislikeUseCase(dislikedUser)
        }
    }
}