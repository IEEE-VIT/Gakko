package com.benrostudios.gakko.ui.home.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.ClassroomJoinRequest
import com.benrostudios.gakko.data.repository.NotificationsRepo

class NotificationsViewModel(
    private val notificationsRepo: NotificationsRepo
) : ViewModel() {

    val requestList = MutableLiveData<List<ClassroomJoinRequest>>()

    init{
        notificationsRepo.requests.observeForever {
            requestList.postValue(it)
        }
    }

    suspend fun test(teachersList: List<String>){
        notificationsRepo.getRequests(teachersList)
    }

    suspend fun dello(classroomId: String , person:String){
        notificationsRepo.rejectRequest(classroomId,person)
    }

    suspend fun accepto(classroomId: String,person: String){
        notificationsRepo.acceptRequest(classroomId,person)
    }
}
