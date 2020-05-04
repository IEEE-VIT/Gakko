package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.ClassroomJoinRequest
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.internal.EMPTY_REQUEST

class NotificationsRepoImpl : NotificationsRepo {

    private val _requests = MutableLiveData<List<ClassroomJoinRequest>>()
    private val _response = MutableLiveData<Boolean>()
    private val _requestBuffer = mutableListOf<ClassroomJoinRequest>()
    private lateinit var databaseReference: DatabaseReference

    override val requests: LiveData<List<ClassroomJoinRequest>>
        get() = _requests
    override val response: LiveData<Boolean>
        get() = _response

    override suspend fun getRequests(classList: List<String>) {
        for (classroom in classList) {
            databaseReference = Firebase.database.getReference("/classrooms/$classroom/requests")
            var requestFetcher = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (x in p0.children) {
                            var incomingObj: Map<String, String> = x.value as Map<String, String>
                            var realTimeObj = incomingObj.toList()
                            userFetcher(
                                classroom,
                                realTimeObj[0].first,
                                realTimeObj[0].second.toLong()
                            )
                        }
                    }
                }

            }
            databaseReference.addListenerForSingleValueEvent(requestFetcher)
        }
    }

    fun userFetcher(classroomId: String, phone: String, timeStamp: Long) {
        databaseReference = Firebase.database.getReference("/users/$phone")
        var userFetcherObj = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var user = p0.getValue(User::class.java)
                var classroomJoinRequest = ClassroomJoinRequest(
                    classroomId,
                    user?.displayName ?: "User",
                    phone,
                    timeStamp,
                    user?.profileImage ?: ""
                )
                Log.d("Notification Fetcher", "Formed Class : ${classroomJoinRequest}")
                _requestBuffer.add(classroomJoinRequest)
                _requests.postValue(_requestBuffer)
            }

        }
        databaseReference.addListenerForSingleValueEvent(userFetcherObj)
    }

    override suspend fun acceptRequest(classroom: String, person: String) {
        classroomRequestEraser(classroom, person)
        databaseReference = Firebase.database.getReference("/users/$person/classrooms")
        var userClassroomUpdater = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var classList = p0.value as MutableList<String>
                classList.add(classroom)
                databaseReference = Firebase.database.getReference("/users/$person/classrooms")
                databaseReference.setValue(classList)
            }

        }
        databaseReference.addListenerForSingleValueEvent(userClassroomUpdater)
    }

    override suspend fun rejectRequest(classroom: String, person: String) {
        classroomRequestEraser(classroom, person)
    }

    fun classroomRequestEraser(classroomId: String, person: String) {
        databaseReference = Firebase.database.getReference("/classrooms/$classroomId/requests")
        var reqEraser = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    var reqList = p0.value as List<Map<String, String>>
                    var updatedList: MutableList<Map<String, String>> = reqList.toMutableList()
                    reqList.forEach {
                        if (it.containsKey(person)) {
                            updatedList.remove(it)
                        }
                    }
                    databaseReference =
                        Firebase.database.getReference("/classrooms/$classroomId/requests")
                    databaseReference.setValue(updatedList)
                }
            }

        }
        databaseReference.addListenerForSingleValueEvent(reqEraser)
    }
}