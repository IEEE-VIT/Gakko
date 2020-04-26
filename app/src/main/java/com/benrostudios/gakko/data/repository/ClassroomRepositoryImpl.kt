package com.benrostudios.gakko.data.repository

import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.network.response.GetClassroomIdResponse
import com.benrostudios.gakko.data.network.service.CreateClassroomService
import com.benrostudios.gakko.internal.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClassroomRepositoryImpl(
    private val utils: Utils,
    private val createClassroomService: CreateClassroomService
) : ClassroomRepository {

    private val _classrooms = MutableLiveData<List<Classroom>>()
    private lateinit var databaseReference: DatabaseReference
    private var classroomIds = mutableListOf<String>()
    private var classList = mutableListOf<Classroom>()
    private var _fetchedClassroomId = MutableLiveData<GetClassroomIdResponse>()


    override val classrooms: LiveData<List<Classroom>>
        get() = _classrooms


    override val createClassroomId: LiveData<GetClassroomIdResponse>
        get() = _fetchedClassroomId

    override suspend fun getClassrooms() {
        databaseReference =
            Firebase.database.getReference("/users/${utils.retrieveMobile()}/classrooms")
        val classroomFetcher = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                classroomIds = mutableListOf()
                classList = mutableListOf<Classroom>()
                for (x in p0.children) {
                    classroomIds.add(x.value.toString())
                    Log.d("ClassroomIDS", classroomIds.toString())
                    classroomLoader(x.value.toString())
                }
            }
        }
        databaseReference.addValueEventListener(classroomFetcher)
    }

    override suspend fun fetchClassroomId() {
        val fetchClassroomId = createClassroomService.getClassroomId()
        _fetchedClassroomId.postValue(fetchClassroomId.body())
    }

    override suspend fun createClassroom(classroom: Classroom) {
        databaseReference = Firebase.database.reference
        databaseReference.child("classrooms").child(classroom.classroomID).setValue(classroom)
        databaseReference = Firebase.database.getReference("/users/${utils.retrieveMobile()}")
        classroomIds.add(classroom.classroomID)
        databaseReference.child("classrooms").setValue(classroomIds)
    }

    fun classroomLoader(ids: String) {
        databaseReference = Firebase.database.getReference("/classrooms/$ids")
        Log.d("classroom fetcher", ids)
        val classroomLoader = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val classroom = p0.getValue(Classroom::class.java)
                Log.d("classroom fetcher", classroom.toString())
                classList.add(classroom!!)
                _classrooms.postValue(classList)
            }
        }
        databaseReference.addListenerForSingleValueEvent(classroomLoader)
    }
}