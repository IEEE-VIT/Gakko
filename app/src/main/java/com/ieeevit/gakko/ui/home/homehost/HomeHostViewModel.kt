package com.ieeevit.gakko.ui.home.homehost

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.repository.ListenerRepo

class HomeHostViewModel(
    private val listenerRepo: ListenerRepo
) : ViewModel() {
    val goBackToClassroomDisplay
        get() = listenerRepo.switchToClassroomDisplay
    fun setClassroomSwitch(truth : Boolean){
        listenerRepo.switchClassroomDisplay(truth)
    }
}
