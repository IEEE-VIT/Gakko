package com.benrostudios.gakko.ui.home.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.NotificationsRepo


@Suppress("UNCHECKED_CAST")
class NotificationsViewModelFactory(
    private val notificationsRepo: NotificationsRepo
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationsViewModel(notificationsRepo) as T
    }
}