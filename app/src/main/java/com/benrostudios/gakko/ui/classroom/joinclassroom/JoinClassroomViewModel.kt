package com.benrostudios.gakko.ui.classroom.joinclassroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.repository.ClassroomRepository

class JoinClassroomViewModel (
    private val classroomRepository: ClassroomRepository
): ViewModel() {
    val _user_classroom_ids = MutableLiveData<List<String>>()
    val usrJoinClassroomResponse = MutableLiveData<Boolean>()
    val classExistenceResponse = MutableLiveData<Boolean>()

    init {
        classroomRepository.userClassroomIds.observeForever {
            Log.d("JoinClassroomViewModel","The ViewModel recieved $it")
            _user_classroom_ids.postValue(it)
        }
        classroomRepository.joinClassroomResponse.observeForever {
            usrJoinClassroomResponse.postValue(it)
        }
        classroomRepository.classroomExistenceResponse.observeForever {
            classExistenceResponse.postValue(it)
        }
    }


    suspend fun joinClass(classCode: String){
        classroomRepository.joinClassroom(classCode)
        Log.d("JoinClassroomViewModel","The reequested Classcode is $classCode")
    }



}
