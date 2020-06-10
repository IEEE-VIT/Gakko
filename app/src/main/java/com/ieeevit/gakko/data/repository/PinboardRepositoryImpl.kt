package com.ieeevit.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ieeevit.gakko.data.models.Material
import com.google.firebase.database.*

class PinboardRepositoryImpl : PinboardRepository {
    private val _materialsList = MutableLiveData<List<Material>>()
    private val list = mutableListOf<Material>()
    private lateinit var databaseReference: DatabaseReference
    override val materialsList: LiveData<List<Material>>
        get() = _materialsList

    override suspend fun getMaterialsList(classId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("pinboards/$classId/")
        val valueEventListener: ValueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for(x: DataSnapshot in p0.children) {
                    val material: Material = x.getValue(Material::class.java)!!
                    list.add(material)
                }
                _materialsList.postValue(list)
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }
}