package com.ieeevit.gakko.ui.auth.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential

class VerificationViewModel(
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

    fun getUser(phone: String){
        authRepository.checkUser(phone)
    }

    suspend fun signInWithFirebase(credential: PhoneAuthCredential) {
        authRepository.signIn(credential)
    }
}
