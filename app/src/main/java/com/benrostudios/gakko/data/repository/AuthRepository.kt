package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.PhoneAuthCredential

interface AuthRepository {
    suspend fun signIn(credential: PhoneAuthCredential)
    fun getAuthStatus(): LiveData<Boolean>
}