package com.ieeevit.gakko.ui.chat.chatdisplay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.ChatRepository


@Suppress("UNCHECKED_CAST")
class ChatInterfaceViewModelFactory(
    private val chatRepository: ChatRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatInterfaceViewModel(chatRepository) as T
    }
}