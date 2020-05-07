package com.benrostudios.gakko.ui.home.proflie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.ProfileRepository


@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(profileRepository) as T
    }
}