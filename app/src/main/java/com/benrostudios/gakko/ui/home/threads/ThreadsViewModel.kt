package com.benrostudios.gakko.ui.home.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.ThreadsRepository

class ThreadsViewModel(private val threadsRepository: ThreadsRepository) : ViewModel() {

    private val _threads = MutableLiveData<List<Threads>>()
    private val _threadClassroom =  MutableLiveData<Classroom>()
    private val _threadUser = MutableLiveData<User>()

    val threads: LiveData<List<Threads>>
        get() = _threads
    val threadClassroom: LiveData<Classroom>
        get() = _threadClassroom
    val threadUser: LiveData<User>
        get() = _threadUser

    init {
        threadsRepository.threads.observeForever { it ->
            _threads.postValue(it)
        }
        threadsRepository.threadClassroom.observeForever {
            _threadClassroom.postValue(it)
        }
        threadsRepository.threadUser.observeForever {
            _threadUser.postValue(it)
        }
    }

    suspend fun getThreads(threadsId: String) {
        threadsRepository.getThreads(threadsId)
    }

    suspend fun getThreadClassroom(classroomId: String) {
        threadsRepository.getThreadsClassroom(classroomId)
    }

    suspend fun getThreadUser(userId: String) {
        threadsRepository.getThreadUser(userId)
    }

    suspend fun postThread(thread: Threads, threadsId: String) {
        threadsRepository.postThread(thread, threadsId)
    }

}
