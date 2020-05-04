package com.benrostudios.gakko.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Material
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class MaterialsRepositoryImpl : MaterialsRepository {
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    var databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val _downloadURL = MutableLiveData<String>()

    override val downloadURL: LiveData<String>
        get() = _downloadURL

    override suspend fun uploadMaterial(uri: Uri, phoneNumber: String, displayName: String) {
        val materialUploadingReference: StorageReference = storageReference.child("materials/$phoneNumber/$displayName.pdf/")
        materialUploadingReference.putFile(uri).addOnCompleteListener {
            if(it.isSuccessful && it.isComplete) {
                materialUploadingReference.downloadUrl.addOnCompleteListener {
                    _downloadURL.postValue(it.result.toString())
                }
            }
        }
    }

    override suspend fun uploadMaterialInformation(material: Material, classNumber:String) {
        val materialDetailsUploadingReference: DatabaseReference = databaseReference.child("pinboards/$classNumber/")
        materialDetailsUploadingReference.push().setValue(material)
    }
}