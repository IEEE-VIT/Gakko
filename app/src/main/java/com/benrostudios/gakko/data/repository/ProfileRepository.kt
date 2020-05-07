package com.benrostudios.gakko.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.User

interface ProfileRepository {
    val response: LiveData<Boolean>
    val user : LiveData<User>
    suspend fun uploadProfilePic(uri: Uri, person: String)
    suspend fun getUser(person: String)
    suspend fun updateProfile(name: String, person:String , displayName: String)
}