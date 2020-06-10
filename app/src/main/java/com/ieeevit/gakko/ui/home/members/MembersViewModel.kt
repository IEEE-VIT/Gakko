package com.ieeevit.gakko.ui.home.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.models.Members
import com.ieeevit.gakko.data.repository.MembersRepository
import com.ieeevit.gakko.data.repository.ThreadsRepository

class MembersViewModel(
    private val threadsRepository: ThreadsRepository,
    private val membersRepository: MembersRepository
) : ViewModel() {
     val teachersList = MutableLiveData<List<Members>>()
     val studentsList = MutableLiveData<List<Members>>()
     val classroom = MutableLiveData<Classroom>()

    init {
        membersRepository.studentsList.observeForever {
            studentsList.postValue(it)
        }
        membersRepository.teachersList.observeForever {
            teachersList.postValue(it)
        }
        threadsRepository.threadClassroom.observeForever {
            classroom.postValue(it)
        }
    }

    suspend fun getTeachersList(phoneList: List<String>){
        membersRepository.getMembers(phoneList,"teacher")
    }

    suspend fun getStudentsList(phoneList: List<String>){
        membersRepository.getMembers(phoneList, "student")
    }

    suspend fun getClassroom(classroomId: String){
        threadsRepository.getThreadsClassroom(classroomId)
    }

}
