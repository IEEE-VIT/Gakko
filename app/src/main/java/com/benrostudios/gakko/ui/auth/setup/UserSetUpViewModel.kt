package com.benrostudios.gakko.ui.auth.setup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.AuthRepository

class UserSetUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val response = MutableLiveData<Boolean>()
    val profilePictureUrl = MutableLiveData<String>()
    suspend fun registerUser(user: User){
        authRepository.registerUser(user)
    }

    init {
        authRepository.userResponse.observeForever {
            response.postValue(it)
        }
        authRepository.profilePictureUrl.observeForever {
            profilePictureUrl.postValue(it)
        }
    }

    suspend fun uploadProfilePic(uri: Uri,person: String ){
        authRepository.uploadUserProfilePicture(uri, person)
    }
}
