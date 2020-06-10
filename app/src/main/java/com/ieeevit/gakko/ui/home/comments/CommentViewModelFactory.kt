package com.ieeevit.gakko.ui.home.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.CommentsRepository
import com.ieeevit.gakko.data.repository.ThreadsRepository

@Suppress("UNCHECKED_CAST")
class CommentViewModelFactory(private val commentsRepository: CommentsRepository, private val threadsRepository: ThreadsRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentViewModel(commentsRepository, threadsRepository) as T
    }
}