package com.ieeevit.gakko.ui.home.homehost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.ListenerRepo


@Suppress("UNCHECKED_CAST")
class HomeHostViewModelFactory(
    private val listenerRepo: ListenerRepo
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeHostViewModel(listenerRepo) as T
    }
}