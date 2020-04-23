package com.benrostudios.gakko.ui.auth.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.AuthRepository


class UserSetUpViewModelFactory (
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserSetUpViewModel(authRepository) as T
    }
}