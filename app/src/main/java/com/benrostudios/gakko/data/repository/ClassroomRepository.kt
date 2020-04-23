package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom

interface ClassroomRepository {
    val classrooms: LiveData<List<Classroom>>
    suspend fun getClassrooms()
}