package com.ieeevit.gakko.ui.classroom.joinclassroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.ClassroomRepository



@Suppress("UNCHECKED_CAST")
class JoinClassroomViewModelFactory(
    private val classroomRepository: ClassroomRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return JoinClassroomViewModel(classroomRepository) as T
    }
}