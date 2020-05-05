package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.*

class TodoRepositoryImpl : TodoRepository {
    private val _user = MutableLiveData<User>()
    private val _classroom = MutableLiveData<Classroom>()
    private val _todo = MutableLiveData<Material>()
    private lateinit var databaseReference: DatabaseReference

    override val user: LiveData<User>
        get() = _user
    override val classroom: LiveData<Classroom>
        get() = _classroom
    override val todo: LiveData<Material>
        get() = _todo

    override suspend fun getUser(userId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users/$userId/")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
               _user.postValue(p0.getValue(User::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }

    override suspend fun getClassrooms(classId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("classrooms/$classId/")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                _classroom.postValue(p0.getValue(Classroom::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)

    }

    override suspend fun getTodo(todoId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("pinboards/$todoId/")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                _todo.postValue(p0.getValue(Material::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }
}