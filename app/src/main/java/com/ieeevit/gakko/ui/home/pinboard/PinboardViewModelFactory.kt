package com.ieeevit.gakko.ui.home.pinboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ieeevit.gakko.data.repository.PinboardRepository

@Suppress("UNCHECKED_CAST")
class PinboardViewModelFactory(private val pinboardRepository: PinboardRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PinboardViewModel(pinboardRepository) as T
    }
}