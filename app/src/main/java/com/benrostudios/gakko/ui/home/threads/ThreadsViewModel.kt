package com.benrostudios.gakko.ui.home.threads

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.repository.ThreadsRepository

class ThreadsViewModel(private val threadsRepository: ThreadsRepository) : ViewModel() {

    private val _threads = MutableLiveData<List<Threads>>()

    val threads: LiveData<List<Threads>>
        get() = _threads

    init {
        threadsRepository.threads.observeForever {
            _threads.postValue(it)
        }
    }

    suspend fun getThreads(threadsId: String) {
        threadsRepository.getThreads(threadsId)
    }

    suspend fun postThread(thread: Threads, threadsId: String) {
        threadsRepository.postThread(thread, threadsId)
    }

    suspend fun postComment(comment: Comments, threadsId: String, specificThreadsId: String) {
        threadsRepository.postComment(comment, threadsId, specificThreadsId)
    }
}
