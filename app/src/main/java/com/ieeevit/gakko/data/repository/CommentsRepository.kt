package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import com.ieeevit.gakko.data.models.Comments
import com.ieeevit.gakko.data.models.Threads
import com.ieeevit.gakko.data.models.User

interface CommentsRepository {
    val specificThread: LiveData<Threads>
    val commentUser: LiveData<User>
    val threadUser: LiveData<User>
    suspend fun getCommentUser(userID: String)
    suspend fun getThreadUser(userID: String)
    suspend fun getSpecificThread(threadId: String, specificThreadId: String)
    suspend fun postComment(comment: Comments, threadId: String, specificThreadId: String)
}