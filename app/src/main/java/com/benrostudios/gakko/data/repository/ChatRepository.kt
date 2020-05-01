package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.ChatMessage

interface ChatRepository {
    val chatMessages: LiveData<List<ChatMessage>>
    suspend fun sendMessage(message: ChatMessage)
    suspend fun receiveMessage()
}