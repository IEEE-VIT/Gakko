package com.benrostudios.gakko.ui.home.threads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.ThreadsRepository

@Suppress("UNCHECKED_CAST")
class ThreadsViewModelFactory(
    private val threadsRepository: ThreadsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ThreadsViewModel(threadsRepository) as T
    }
}