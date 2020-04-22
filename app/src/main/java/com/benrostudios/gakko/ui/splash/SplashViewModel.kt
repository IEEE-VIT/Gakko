package com.benrostudios.gakko.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.AuthRepository
import com.benrostudios.gakko.data.repository.FirebaseRepository

class SplashViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val userResponse = MutableLiveData<Boolean>()

    init{
        firebaseRepository.userResponse.observeForever {
            userResponse.postValue(it)
        }
    }

    fun getUser(phone: String){
        firebaseRepository.checkUser(phone)
    }




}