package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import com.ieeevit.gakko.data.models.ChatMessage
import com.ieeevit.gakko.data.models.User

interface ChatRepository {
    val chatMessages: LiveData<List<ChatMessage>>
    val recipientUser: LiveData<User>
    suspend fun sendMessage(message: ChatMessage)
    suspend fun receiveMessage()
    suspend fun resetChat()
}