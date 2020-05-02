package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User

interface CommentsRepository {
    val specificThread: LiveData<Threads>
    val commentUser: LiveData<User>
    suspend fun getCommentUser(userID: String)
    suspend fun getSpecificThread(threadId: String, specificThreadId: String)
    suspend fun postComment(comment: Comments, threadId: String, specificThreadId: String)
}