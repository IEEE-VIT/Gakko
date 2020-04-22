package com.benrostudios.gakko.ui.auth.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.FirebaseRepository


class UserSetUpViewModelFactory (
    private val firebaseRepository: FirebaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserSetUpViewModel(firebaseRepository) as T
    }
}