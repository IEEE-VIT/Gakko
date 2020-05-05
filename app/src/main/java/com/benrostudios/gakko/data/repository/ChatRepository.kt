package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.ChatMessage
import com.benrostudios.gakko.data.models.User

interface ChatRepository {
    val chatMessages: LiveData<List<ChatMessage>>
    val recipientUser: LiveData<User>
    suspend fun sendMessage(message: ChatMessage)
    suspend fun receiveMessage()
}