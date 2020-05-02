package com.benrostudios.gakko.ui.home.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.CommentsRepository
import com.benrostudios.gakko.data.repository.ThreadsRepository

@Suppress("UNCHECKED_CAST")
class CommentViewModelFactory(private val commentsRepository: CommentsRepository, private val threadsRepository: ThreadsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentViewModel(commentsRepository, threadsRepository) as T
    }
}