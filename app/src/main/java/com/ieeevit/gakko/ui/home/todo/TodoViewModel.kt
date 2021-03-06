package com.ieeevit.gakko.ui.home.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Classroom
import com.ieeevit.gakko.data.models.Material
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.data.repository.TodoRepository

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()
    private val _classroom = MutableLiveData<List<Classroom>>()
    private val _todo = MutableLiveData<List<Material>>()

    val user: LiveData<User>
        get() = _user
    val classroom: LiveData<List<Classroom>>
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
    suspend fun getClassroom(classIdList: List<String>) {
        todoRepository.getClassrooms(classIdList)
    }
    suspend fun getTodo(todoIdList: List<String>) {
        todoRepository.getTodo(todoIdList)
    }
}
