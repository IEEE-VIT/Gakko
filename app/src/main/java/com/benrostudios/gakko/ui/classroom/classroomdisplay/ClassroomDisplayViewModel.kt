package com.benrostudios.gakko.ui.classroom.classroomdisplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.repository.ClassroomRepository

class ClassroomDisplayViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    var classroom:LiveData<List<Classroom>> = MutableLiveData<List<Classroom>>()
    init {
        classroomRepository.classrooms.observeForever {
            Log.d("Recieved Classroom Obj",it.toString())
        }
    }
    suspend fun test() {
        classroomRepository.getClassrooms()
    }
}
