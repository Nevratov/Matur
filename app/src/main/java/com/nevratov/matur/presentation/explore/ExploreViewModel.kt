package com.nevratov.matur.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevratov.matur.domain.entity.User
import com.nevratov.matur.domain.usecases.DislikeUseCase
import com.nevratov.matur.domain.usecases.GetUsersToExploreUseCase
import com.nevratov.matur.domain.usecases.LikeUseCase
import com.nevratov.matur.extentions.mergeWith
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExploreViewModel @Inject constructor(
    getUsersToExploreUseCase: GetUsersToExploreUseCase,
    private val likeUseCase: LikeUseCase,
    private val dislikeUseCase: DislikeUseCase
): ViewModel() {

    private val actionEvent = MutableSharedFlow<ExploreScreenState>()

    val state = getUsersToExploreUseCase()
        .map { users ->
            if (users == null) ExploreScreenState.ContentIsEmpty
            else ExploreScreenState.Content(users) as ExploreScreenState
        }.mergeWith(actionEvent)


    fun like(likedUser: User) {
        viewModelScope.launch {
            actionEvent.emit(ExploreScreenState.Loading)
            delay(10)
            likeUseCase(likedUser)
        }
    }

    fun dislike(dislikedUser: User) {
        viewModelScope.launch {
            actionEvent.emit(ExploreScreenState.Loading)
            delay(10)
            dislikeUseCase(dislikedUser)
        }
    }
}