package com.benrostudios.gakko.ui.home.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.TodoRepository

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _classroom = MutableLiveData<Classroom>()
    private val _todo = MutableLiveData<List<Material>>()

    val user: LiveData<User>
        get() = _user
    val classroom: LiveData<Classroom>
        get() = _classroom
    val todo: LiveData<List<Material>>
        get() = _todo

    init {
        todoRepository.user.observeForever {
            _user.postValue(it)
        }
        todoRepository.classroom.observeForever {
            _classroom.postValue(it)
        }
        todoRepository.todo.observeForever {
            _todo.postValue(it)
        }
    }

    suspend fun getUser(userId: String) {
        todoRepository.getUser(userId)
    }
    suspend fun getClassroom(classId: String) {
        todoRepository.getClassrooms(classId)
    }
    suspend fun getTodo(todoId: String) {
        todoRepository.getTodo(todoId)
    }
}
