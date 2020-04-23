package com.benrostudios.gakko.data.repository

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.internal.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClassroomRepositoryImpl(
    private val utils: Utils
) : ClassroomRepository {

    private val _classrooms = MutableLiveData<List<Classroom>>()
    private lateinit var databaseReference: DatabaseReference
    private var classroomIds = mutableListOf<String>()
    private  var classList = mutableListOf<Classroom>()
    override val classrooms: LiveData<List<Classroom>>
        get() = _classrooms

    override suspend fun getClassrooms() {
        databaseReference = Firebase.database.getReference("/users/${utils.retrieveMobile()}/classrooms")
        val classroomFetcher = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                for(x in p0.children){
                    classroomLoader(x.value.toString())
                }
            }
        }
        databaseReference.addListenerForSingleValueEvent(classroomFetcher)

    }

    fun classroomLoader(value: String){
        classroomIds.add(value)
        for(ids in classroomIds){
            databaseReference = Firebase.database.getReference("/classrooms/$ids")
            val classroomLoader = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }
                override fun onDataChange(p0: DataSnapshot) {
                    val classroom = p0.getValue(Classroom::class.java)
                    classList.add(classroom!!)
                    _classrooms.postValue(classList)
                }
            }
            databaseReference.addListenerForSingleValueEvent(classroomLoader)
        }

    }
}