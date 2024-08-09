package com.nevratov.matur.presentation.main

import androidx.lifecycle.ViewModel
import com.nevratov.matur.domain.usecases.GetAuthStateFlowUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getAuthStateFlowUseCase: GetAuthStateFlowUseCase

) : ViewModel() {

    val authState = getAuthStateFlowUseCase()
}