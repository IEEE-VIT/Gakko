package com.benrostudios.gakko.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val response = MutableLiveData<Boolean>()


    suspend fun signInWithFirebase(credential: PhoneAuthCredential) {
        authRepository.signIn(credential)
    }

    fun getAuthStatus(): LiveData<Boolean> {
        authRepository.getAuthStatus().observeForever {
            response.postValue(it)
        }
        return response
    }


}
