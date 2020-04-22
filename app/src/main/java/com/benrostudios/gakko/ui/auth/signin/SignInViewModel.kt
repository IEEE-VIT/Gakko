package com.benrostudios.gakko.ui.auth.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.AuthRepository
import com.benrostudios.gakko.data.repository.FirebaseRepository
import com.google.firebase.auth.PhoneAuthCredential

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    val response = MutableLiveData<Boolean>()
    val userResponse = MutableLiveData<Boolean>()

    init{
        authRepository.getAuthStatus.observeForever {
            response.postValue(it)
        }
        firebaseRepository.userResponse.observeForever {
            userResponse.postValue(it)
        }
    }

    suspend fun signInWithFirebase(credential: PhoneAuthCredential) {
        authRepository.signIn(credential)
    }

    fun getUser(phone: String){
        firebaseRepository.checkUser(phone)
    }



}
