package com.benrostudios.gakko.ui.home.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.CommentsRepository
import com.benrostudios.gakko.data.repository.ThreadsRepository

class CommentViewModel(private val commentsRepository: CommentsRepository, private val threadsRepository: ThreadsRepository)
    : ViewModel() {

    private val _specificThread = MutableLiveData<Threads>()
    private val _classroom = MutableLiveData<Classroom>()
    private val _commenter = MutableLiveData<User>()

    val specificThread: LiveData<Threads>
        get() = _specificThread
    val commentsClassroom: LiveData<Classroom>
        get() = _classroom
    val commenter: LiveData<User>
        get() = _commenter

    init {
        threadsRepository.threadClassroom.observeForever(Observer {
            _classroom.postValue(it)
        })
        commentsRepository.commentUser.observeForever(Observer {
            _commenter.postValue(it)
        })
        commentsRepository.specificThread.observeForever(Observer {
            _specificThread.postValue(it)
        })
    }

    suspend fun getSpecificThread(threadId: String, specificThreadId: String) {
        commentsRepository.getSpecificThread(threadId, specificThreadId)
    }
    suspend fun postComment(comment: Comments, threadId: String, specificThreadId: String) {
        commentsRepository.postComment(comment, threadId, specificThreadId)
    }
    suspend fun getCommentUser(userId: String) {
        commentsRepository.getCommentUser(userId)
    }
}
