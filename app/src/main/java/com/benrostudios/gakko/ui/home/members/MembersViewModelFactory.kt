package com.benrostudios.gakko.ui.home.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.MembersRepository
import com.benrostudios.gakko.data.repository.ThreadsRepository

@Suppress("UNCHECKED_CAST")
class MembersViewModelFactory(
    private val threadsRepository: ThreadsRepository,
    private val membersRepository: MembersRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MembersViewModel(threadsRepository,membersRepository) as T
    }
}