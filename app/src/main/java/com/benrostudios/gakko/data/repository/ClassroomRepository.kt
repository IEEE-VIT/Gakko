package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.network.response.GetClassroomIdResponse

interface ClassroomRepository {
    val classrooms: LiveData<List<Classroom>>
    val createClassroomId: LiveData<GetClassroomIdResponse>
    val userClassroomIds: LiveData<List<String>>
    val joinClassroomResponse: LiveData<Boolean>
    suspend fun getClassrooms()
    suspend fun fetchClassroomId()
    suspend fun createClassroom(classroom: Classroom)
    suspend fun joinClassroom(classCode: String)


}