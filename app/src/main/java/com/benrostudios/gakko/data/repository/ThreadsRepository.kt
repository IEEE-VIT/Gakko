package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads

interface ThreadsRepository {
    val threads: LiveData<List<Threads>>
    suspend fun getThreads(threadId: String)
    suspend fun postThreads(thread:Threads, threadId: String)
    suspend fun postComments(comment:Comments, threadId: String, specificThreadId: String)
}