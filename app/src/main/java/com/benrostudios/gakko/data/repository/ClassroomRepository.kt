package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.network.response.GetClassroomIdResponse

interface ClassroomRepository {
    val classrooms: LiveData<List<Classroom>>
    val createClassroomId: LiveData<GetClassroomIdResponse>
    suspend fun getClassrooms()
    suspend fun fetchClassroomId()

}