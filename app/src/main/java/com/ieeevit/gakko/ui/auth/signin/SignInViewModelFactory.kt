package com.ieeevit.gakko.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.AuthRepository


@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory (
    private val authRepository: AuthRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(authRepository) as T
    }
}