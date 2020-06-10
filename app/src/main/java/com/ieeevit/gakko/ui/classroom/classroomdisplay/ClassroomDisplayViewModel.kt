package com.ieeevit.gakko.ui.classroom.classroomdisplay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.repository.ClassroomRepository

class ClassroomDisplayViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {

    var classroom = MutableLiveData<List<Classroom>>()
    init {
        classroomRepository.classrooms.observeForever {
            classroom.postValue(it)
        }
    }
    suspend fun test(){
        classroomRepository.getClassrooms()
    }
}
