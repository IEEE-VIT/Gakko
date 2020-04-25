package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User

interface ThreadsRepository {
    val threads: LiveData<List<Threads>>
    val threadClassroom: LiveData<Classroom>
    val threadUser: LiveData<User>
    suspend fun getThreads(threadId: String)
    suspend fun getThreadsClassroom(classroomId: String)
    fun getThreadUser(userID: String)
    suspend fun postThread(thread:Threads, threadId: String)
    suspend fun postComment(comment:Comments, threadId: String, specificThreadId: String)
}