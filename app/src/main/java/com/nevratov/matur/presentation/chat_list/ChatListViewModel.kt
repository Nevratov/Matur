package com.nevratov.matur.presentation.chat_list

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nevratov.matur.domain.usecases.GetChatListUseCase
import com.nevratov.matur.domain.usecases.GetUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ChatListViewModel @Inject constructor(
    getChatListUseCase: GetChatListUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    val state = getChatListUseCase()
        .map {
            ChatListScreenState.Content(chatList = it)
        }
        .onStart {
            ChatListScreenState.Loading }
        .catch {
            Log.d("Catching error", "catch")
        }

    fun getUser() = getUserUseCase()
}