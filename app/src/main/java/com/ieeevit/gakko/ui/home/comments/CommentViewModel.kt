package com.ieeevit.gakko.ui.home.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.models.Comments
import com.ieeevit.gakko.data.models.Threads
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.data.repository.CommentsRepository
import com.ieeevit.gakko.data.repository.ThreadsRepository

class CommentViewModel(private val commentsRepository: CommentsRepository, private val threadsRepository: ThreadsRepository)
    : ViewModel() {

    private val _specificThread = MutableLiveData<Threads>()
    private val _classroom = MutableLiveData<Classroom>()
    private val _commenter = MutableLiveData<User>()
    private val _threadUser = MutableLiveData<User>()

    val specificThread: LiveData<Threads>
        get() = _specificThread
    val commentsClassroom: LiveData<Classroom>
        get() = _classroom
    val commenter: LiveData<User>
        get() = _commenter
    val threadUser: LiveData<User>
        get() = _threadUser

    init {
        threadsRepository.threadClassroom.observeForever {
            _classroom.postValue(it)
        }
        commentsRepository.commentUser.observeForever {
            _commenter.postValue(it)
        }
        commentsRepository.specificThread.observeForever {
            _specificThread.postValue(it)
        }
        commentsRepository.threadUser.observeForever {
            _threadUser.postValue(it)
        }
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
    suspend fun getThreadUser(userId: String) {
        commentsRepository.getThreadUser(userId)
    }
}
