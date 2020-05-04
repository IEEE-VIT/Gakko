package com.benrostudios.gakko.ui.home.material

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benrostudios.gakko.data.repository.MaterialsRepository

@Suppress("UNCHECKED_CAST")
class MaterialViewModelFactory(private val materialsRepository: MaterialsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MaterialViewModel(materialsRepository) as T
    }
}