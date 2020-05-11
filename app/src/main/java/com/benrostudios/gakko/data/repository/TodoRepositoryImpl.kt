package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Material
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.*

class TodoRepositoryImpl : TodoRepository {
    private val _user = MutableLiveData<User>()
    private val _classroom = MutableLiveData<List<Classroom>>()
    private val _todo = MutableLiveData<List<Material>>()
    private lateinit var databaseReference: DatabaseReference
    private var classroomsList = mutableListOf<Classroom>()
    private var todoList = mutableListOf<Material>()

    override val user: LiveData<User>
        get() = _user
    override val classroom: LiveData<List<Classroom>>
        get() = _classroom
    override val todo: LiveData<List<Material>>
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

    override suspend fun getClassrooms(classIdList: List<String>) {
        classroomsList.clear()
        for(classId: String in classIdList) {
            databaseReference = FirebaseDatabase.getInstance().getReference("classrooms/$classId/")
            val valueEventListener: ValueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if(! classroomsList.contains(p0.getValue(Classroom::class.java))) {
                        classroomsList.add(p0.getValue(Classroom::class.java)!!)
                        _classroom.postValue(classroomsList)
                    }
                }
            }
            databaseReference.addValueEventListener(valueEventListener)
        }
    }

    override suspend fun getTodo(todoIdList: List<String>) {
        todoList.clear()
        for (todoId: String in todoIdList) {
            databaseReference = FirebaseDatabase.getInstance().getReference("pinboards/$todoId/")
            val valueEventListener: ValueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (x: DataSnapshot in p0.children) {
                        if(! todoList.contains(p0.getValue(Material::class.java))) {
                            todoList.add(x.getValue(Material::class.java)!!)
                        }
                    }
                    if(todoList.isNullOrEmpty()) {
                        _todo.postValue(null)
                    } else {
                        _todo.postValue(todoList)
                    }
                }
            }
            databaseReference.addValueEventListener(valueEventListener)
        }
    }
}