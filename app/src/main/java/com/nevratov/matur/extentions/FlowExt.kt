package com.nevratov.matur.extentions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge

fun <T> Flow<T>.mergeWith(flow: Flow<T>) = merge(this, flow)