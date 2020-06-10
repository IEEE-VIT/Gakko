package com.ieeevit.gakko.ui.home.material

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ieeevit.gakko.data.models.Material
import com.ieeevit.gakko.data.repository.MaterialsRepository

class MaterialViewModel(private val materialsRepository: MaterialsRepository) : ViewModel() {

    private val _downloadURL = MutableLiveData<String>()

    init {
        materialsRepository.downloadURL.observeForever(Observer {
            _downloadURL.postValue(it)
        })
    }

    val downloadURL: LiveData<String>
        get() = _downloadURL

    suspend fun uploadMaterial(uri: Uri, phoneNumber: String, displayName:String) {
        materialsRepository.uploadMaterial(uri, phoneNumber, displayName)
    }

    suspend fun uploadMaterialInformation(material: Material, classNumber: String) {
        materialsRepository.uploadMaterialInformation(material, classNumber)
    }
}
