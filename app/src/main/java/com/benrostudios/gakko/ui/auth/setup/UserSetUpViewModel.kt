package com.benrostudios.gakko.ui.auth.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.FirebaseRepository

class UserSetUpViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val response = MutableLiveData<Boolean>()
    suspend fun registerUser(user: User){
        firebaseRepository.registerUser(user)
    }

    init {
        firebaseRepository.userResponse.observeForever {
            response.postValue(it)
        }
    }
}
