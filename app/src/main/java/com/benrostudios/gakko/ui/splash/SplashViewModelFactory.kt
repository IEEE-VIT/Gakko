package com.benrostudios.gakko.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.FirebaseRepository


@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(
    private val firebaseRepository: FirebaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(firebaseRepository) as T
    }
}