package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import com.benrostudios.gakko.data.models.ClassroomJoinRequest

interface NotificationsRepo {
    val requests: LiveData<List<ClassroomJoinRequest>>
    val response: LiveData<Boolean>
    suspend fun getRequests(classList: List<String>)
    suspend fun acceptRequest(classroom: String , person: String)
    suspend fun rejectRequest(classroom: String,person: String)
}