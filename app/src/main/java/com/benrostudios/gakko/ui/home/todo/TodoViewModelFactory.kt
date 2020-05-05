package com.benrostudios.gakko.ui.home.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.TodoRepository

@Suppress("UNCHECKED_CAST")
class TodoViewModelFactory(private val todoRepository: TodoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TodoViewModel(todoRepository) as T
    }
}