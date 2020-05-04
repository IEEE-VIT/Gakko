package com.benrostudios.gakko.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.User
import com.google.firebase.auth.PhoneAuthCredential

interface AuthRepository {
    suspend fun signIn(credential: PhoneAuthCredential)
    val getAuthStatus: LiveData<Boolean>
    fun checkUser(phone: String)
    val userResponse: LiveData<Boolean>
    suspend fun registerUser(user: User)
    suspend fun uploadUserProfilePicture(uri: Uri, person: String)
    val profilePictureUrl: LiveData<String>
}