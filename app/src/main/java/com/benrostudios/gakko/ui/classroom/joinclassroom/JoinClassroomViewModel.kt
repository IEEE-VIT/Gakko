package com.benrostudios.gakko.ui.classroom.joinclassroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.ClassroomRepository

class JoinClassroomViewModel (
    private val classroomRepository: ClassroomRepository
): ViewModel() {
    val _user_classroom_ids = MutableLiveData<List<String>>()
    val usrJoinClassroomResponse = MutableLiveData<Boolean>()

    init {
        classroomRepository.userClassroomIds.observeForever {
            _user_classroom_ids.postValue(it)
        }
        classroomRepository.joinClassroomResponse.observeForever {
            usrJoinClassroomResponse.postValue(it)
        }
    }

    suspend fun joinClass(classCode: String){
        classroomRepository.joinClassroom(classCode)
    }

}
