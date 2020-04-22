package com.benrostudios.gakko.ui.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.AuthRepository
import com.benrostudios.gakko.data.repository.FirebaseRepository


@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory (
    private val authRepository: AuthRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignInViewModel(authRepository,firebaseRepository) as T
    }
}