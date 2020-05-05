package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.User

interface TodoRepository {
    val user: LiveData<User>
    val classroom: LiveData<Classroom>
    val todo: LiveData<Material>

    suspend fun getUser(userId: String)
    suspend fun getClassrooms(classId: String)
    suspend fun getTodo(classId: String)
}