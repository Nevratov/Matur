package com.nevratov.matur.presentation.explore

import com.nevratov.matur.domain.entity.User

sealed class ExploreScreenState {

    data object Initial: ExploreScreenState()

    data object Loading: ExploreScreenState()

    data object ContentIsEmpty: ExploreScreenState()

    data class Content(val exploreUser: User): ExploreScreenState()
}