package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.models.Threads
import com.ieeevit.gakko.data.models.User

interface ThreadsRepository {
    val threads: LiveData<List<Threads>>
    val threadClassroom: LiveData<Classroom>
    val threadUser: LiveData<User>
    suspend fun getThreads(threadId: String)
    suspend fun getThreadsClassroom(classroomId: String)
    suspend fun getThreadUser(userID: String)
    suspend fun postThread(thread:Threads, threadId: String)
}