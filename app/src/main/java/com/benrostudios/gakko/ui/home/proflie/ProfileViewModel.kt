package com.benrostudios.gakko.ui.home.proflie

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val userObject = MutableLiveData<User>()

    init{
        profileRepository.user.observeForever {
            userObject.postValue(it)
        }
    }

    suspend fun fetchUser(person: String){
        profileRepository.getUser(person)
    }

    suspend fun uploadProfilePic(uri: Uri, person: String){
        profileRepository.uploadProfilePic(uri , person)
    }

    suspend fun updateProfile(name: String , displayName: String , person: String){
        profileRepository.updateProfile(name , person, displayName)
    }

}
