package com.benrostudios.gakko.ui.auth.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val response = MutableLiveData<Boolean>()
    val userResponse = MutableLiveData<Boolean>()

    init{
        authRepository.getAuthStatus.observeForever {
            response.postValue(it)
        }
        authRepository.userResponse.observeForever {
            userResponse.postValue(it)
        }
    }

    suspend fun signInWithFirebase(credential: PhoneAuthCredential) {
        authRepository.signIn(credential)
    }

    fun getUser(phone: String){
        authRepository.checkUser(phone)
    }



}
