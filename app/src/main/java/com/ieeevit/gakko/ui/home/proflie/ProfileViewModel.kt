package com.ieeevit.gakko.ui.home.proflie

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.data.repository.ProfileRepository

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val userObject = MutableLiveData<User>()
    val response = MutableLiveData<Boolean>()

    init{
        profileRepository.user.observeForever {
            userObject.postValue(it)
        }
        profileRepository.response.observeForever {
            response.postValue(it)
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
