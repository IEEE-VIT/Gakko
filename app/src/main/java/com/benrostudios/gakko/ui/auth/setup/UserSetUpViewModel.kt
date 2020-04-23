package com.benrostudios.gakko.ui.auth.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.AuthRepository

class UserSetUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val response = MutableLiveData<Boolean>()
    suspend fun registerUser(user: User){
        authRepository.registerUser(user)
    }

    init {
        authRepository.userResponse.observeForever {
            response.postValue(it)
        }
    }
}
