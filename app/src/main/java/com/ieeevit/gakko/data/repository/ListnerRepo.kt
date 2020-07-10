package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData

interface ListenerRepo {
    fun switchClassroomDisplay(truth: Boolean)
    val switchToClassroomDisplay: LiveData<Boolean>
}