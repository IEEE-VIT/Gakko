package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Members
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MembersRepositoryImpl : MembersRepository {

    private val _teacherList = MutableLiveData<List<Members>>()
    private val _studentsList = MutableLiveData<List<Members>>()
    private lateinit var databaseReference: DatabaseReference

    override val teachersList: LiveData<List<Members>>
        get() = _teacherList
    override val studentsList: LiveData<List<Members>>
        get() = _studentsList


    override suspend fun getMembers(phoneList: List<String>, type: String) {
        var member: Members
        val teachers = mutableListOf<Members>()
        val students = mutableListOf<Members>()
        for (phoneNumbers in phoneList) {
            databaseReference = Firebase.database.getReference("/users/$phoneNumbers")
            val memberLoader = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    member = Members(user!!.name, user.profileImage, user.id)
                    if (type == "teacher") {
                        if(!teachers.contains(member)){
                        teachers.add(member)
                        _teacherList.postValue(teachers)}
                    } else {
                        if(!students.contains(member)){
                        students.add(member)
                        _studentsList.postValue(students)}
                    }
                }
            }
            databaseReference.addValueEventListener(memberLoader)
        }
    }
}