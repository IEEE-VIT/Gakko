package com.ieeevit.gakko.ui.classroom.createclassroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.ClassroomRepository


@Suppress("UNCHECKED_CAST")
public class CreateClassroomViewModelFactory(
    private val classroomRepository: ClassroomRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateClassroomViewModel(classroomRepository) as T
    }
}