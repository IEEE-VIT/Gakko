package com.ieeevit.gakko.ui.classroom.createclassroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.ClassroomRepository
import com.ieeevit.gakko.data.repository.ListenerRepo
import com.ieeevit.gakko.data.repository.ProfileRepository


@Suppress("UNCHECKED_CAST")
public class CreateClassroomViewModelFactory(
    private val classroomRepository: ClassroomRepository,
    private val listenerRepo: ListenerRepo
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateClassroomViewModel(classroomRepository,listenerRepo) as T
    }
}