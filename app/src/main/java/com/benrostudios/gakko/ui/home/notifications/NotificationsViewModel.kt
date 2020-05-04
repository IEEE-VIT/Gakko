package com.benrostudios.gakko.ui.home.notifications

import android.util.Log
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

    suspend fun fetchRequestList(teachersList: List<String>){
        notificationsRepo.getRequests(teachersList)
    }

    suspend fun declineRequests(classroomId: String, person:String){
        notificationsRepo.rejectRequest(classroomId,person)
    }

    suspend fun acceptRequest(classroomId: String, person: String){
        notificationsRepo.acceptRequest(classroomId,person)
    }
}
