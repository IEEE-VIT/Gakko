package com.benrostudios.gakko.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.AuthRepository

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userResponse = MutableLiveData<Boolean>()

    init{
        authRepository.userResponse.observeForever {
            userResponse.postValue(it)
        }
    }

    fun getUser(phone: String){
        authRepository.checkUser(phone)
    }




}