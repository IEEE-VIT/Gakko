package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ListenerRepoImpl : ListenerRepo {
    private val _switchToClassroomDisplay = MutableLiveData<Boolean>()
    override fun switchClassroomDisplay(truth: Boolean) {
       _switchToClassroomDisplay.postValue(truth)
    }

    override val switchToClassroomDisplay: LiveData<Boolean>
        get() = _switchToClassroomDisplay
}