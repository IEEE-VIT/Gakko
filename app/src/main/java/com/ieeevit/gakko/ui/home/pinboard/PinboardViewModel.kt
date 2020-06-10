package com.ieeevit.gakko.ui.home.pinboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Material
import com.ieeevit.gakko.data.repository.PinboardRepository

class PinboardViewModel(private val pinboardRepository: PinboardRepository) : ViewModel() {

    private val _materialsList = MutableLiveData<List<Material>>()

    init {
        pinboardRepository.materialsList.observeForever(Observer {
            _materialsList.postValue(it)
        })
    }

    val materialsList: LiveData<List<Material>>
        get() = _materialsList

    suspend fun getMaterialsList(classId: String) {
        pinboardRepository.getMaterialsList(classId)
    }

}
