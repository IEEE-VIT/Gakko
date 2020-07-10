package com.ieeevit.gakko.ui.home.homehost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeHostViewModel : ViewModel() {

    val goBackToClassroomDisplay = MutableLiveData<Boolean>()


    fun goBackToClassroom(truth : Boolean){
        if(truth){
            goBackToClassroomDisplay.postValue(true)
        }
    }
}
