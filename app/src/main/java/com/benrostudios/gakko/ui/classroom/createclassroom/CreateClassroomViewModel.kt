package com.benrostudios.gakko.ui.classroom.createclassroom

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.network.response.GetClassroomIdResponse
import com.benrostudios.gakko.data.repository.ClassroomRepository

class CreateClassroomViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    private var newClassroomId: String = ""
    val fetchClassroomIdResponse = MutableLiveData<GetClassroomIdResponse>()
    val createClassroomResponse = MutableLiveData<Boolean>()
    private var classroomIds: List<String> = emptyList()

    init {
        classroomRepository.createClassroomId.observeForever {
            fetchClassroomIdResponse.postValue(it)
            Log.d("FetchIdResponse", it.classroomId)
        }
        classroomRepository.userClassroomIds.observeForever {
            classroomIds = it
            if (it.contains(newClassroomId)) {
                createClassroomResponse.postValue(true)
                Log.d("createResponse", it.toString())
            }
        }
    }

    suspend fun createClassroom(newClassroom: Classroom) {
        if (!classroomIds.contains(newClassroom.classroomID)) {
            classroomRepository.createClassroom(newClassroom)
            newClassroomId = newClassroom.classroomID
        }
    }

    suspend fun fetchNewClassroomId() {
        classroomRepository.fetchClassroomId()
    }

}
