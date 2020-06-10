package com.ieeevit.gakko.ui.chat.chatdisplay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.ChatMessage
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.data.repository.ChatRepository

class ChatInterfaceViewModel(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val usrChats = MutableLiveData<List<ChatMessage>>()
    val recipientUser = MutableLiveData<User>()

    init {
        chatRepository.chatMessages.observeForever {
            usrChats.postValue(it)
        }
        chatRepository.recipientUser.observeForever {
            recipientUser.postValue(it)
        }
    }

    suspend fun receiveMessages(){
        chatRepository.receiveMessage()
    }

    suspend fun sendMessage(message: ChatMessage){
        chatRepository.sendMessage(message)
    }
    suspend fun resetChat(){
        chatRepository.resetChat()
    }
}
