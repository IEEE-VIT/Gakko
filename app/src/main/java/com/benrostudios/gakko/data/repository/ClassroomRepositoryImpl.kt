package com.benrostudios.gakko.data.repository

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

    private var _fetchedClassroomId = MutableLiveData<GetClassroomIdResponse>()
    private val _classrooms = MutableLiveData<List<Classroom>>()
    private val _classroomIds = MutableLiveData<List<String>>()
    private val _join_classroom_response = MutableLiveData<Boolean>()
    private lateinit var databaseReference: DatabaseReference
    private var classroomIds = mutableListOf<String>()
    private var classList = mutableListOf<Classroom>()
    private val teachersList = utils.retrieveTeachersList() ?: mutableSetOf<String>()


    override val classrooms: LiveData<List<Classroom>>
        get() = _classrooms


    override val createClassroomId: LiveData<GetClassroomIdResponse>
        get() = _fetchedClassroomId

    override val userClassroomIds: LiveData<List<String>>
        get() = _classroomIds

    override val joinClassroomResponse: LiveData<Boolean>
        get() = _join_classroom_response

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
                    _classroomIds.postValue(classroomIds)
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
        updateUserClassroom(classroom.classroomID)
    }

    override suspend fun joinClassroom(classCode: String) {
        databaseReference = Firebase.database.getReference("/classrooms/$classCode")
        val joinClassroomFetcher = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val classroom = p0.getValue(Classroom::class.java)
                if (classroom?.privacy == false) {
                    databaseReference = Firebase.database.getReference("/classrooms/$classCode")
                    val students: MutableList<String> = classroom.students.toMutableList()
                        ?: mutableListOf(utils.retrieveMobile()!!)
                    if (students.isEmpty()) {
                        students.add(utils.retrieveMobile()!!)
                        databaseReference.child("students").setValue(students)
                    } else {
                        students.add(utils.retrieveMobile()!!)
                        databaseReference.child("students").setValue(students)
                    }
                    updateUserClassroom(classCode)
                    _join_classroom_response.postValue(true)
                } else if(classroom?.privacy == true) {
                    var requests: MutableList<Map<String, String>> =
                        classroom.requests.toMutableList()
                    val unixTime = System.currentTimeMillis() / 1000L
                    requests.add(mapOf(Pair(utils.retrieveMobile() ?: "", unixTime.toString())))
                    databaseReference = Firebase.database.getReference("/classrooms/$classCode")
                    databaseReference.child("requests").setValue(requests)
                    _join_classroom_response.postValue(false)
                }
            }
        }
        databaseReference.addListenerForSingleValueEvent(joinClassroomFetcher)
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
                if(classroom?.teachers?.contains(utils.retrieveMobile()) == true && classroom.privacy){
                    teachersList.add(classroom.classroomID)
                    utils.saveTeacherList(teachersList)
                    Log.d("classroom fetcher","teacher of: ${classroom.classroomID}")
                }
                classList.add(classroom!!)
                _classrooms.postValue(classList)
            }
        }
        databaseReference.addListenerForSingleValueEvent(classroomLoader)
    }

    fun updateUserClassroom(classCode: String) {
        databaseReference = Firebase.database.getReference("/users/${utils.retrieveMobile()}")
        classroomIds.add(classCode)
        databaseReference.child("classrooms").setValue(classroomIds)
    }

}