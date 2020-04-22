package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential

class AuthRepositoryImpl :
    AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val AUTH_REPO_TAG = "AuthRepo"
    private val _response = MutableLiveData<Boolean>()

    override suspend fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(AUTH_REPO_TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    _response.postValue(true)
                } else {
                    Log.w(AUTH_REPO_TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        _response.postValue(false)
                    }
                }
            }
    }

    override val getAuthStatus: LiveData<Boolean>
        get() = _response

}