package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.User

interface TodoRepository {
    val user: LiveData<User>
    val classroom: LiveData<List<Classroom>>
    val todo: LiveData<List<Material>>

    suspend fun getUser(userId: String)
    suspend fun getClassrooms(classIdList: List<String>)
    suspend fun getTodo(todoIdList: List<String>)
}