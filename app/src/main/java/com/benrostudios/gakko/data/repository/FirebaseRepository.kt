package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.User

interface FirebaseRepository {
    fun checkUser(phone: String)
    val userResponse: LiveData<Boolean>
    suspend fun registerUser(user: User)
}