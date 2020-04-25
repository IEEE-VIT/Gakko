package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Classroom
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.*

class ThreadsRepositoryImpl : ThreadsRepository {

    private var _threads = MutableLiveData<List<Threads>>()
    private var _threadClassroom = MutableLiveData<Classroom>()
    private var _threadUser = MutableLiveData<User>()
    private lateinit var databaseReference: DatabaseReference
    val threadList = mutableListOf<Threads>()

    override val threads: LiveData<List<Threads>>
        get() = _threads
    override val threadClassroom: LiveData<Classroom>
        get() = _threadClassroom
    override val threadUser: LiveData<User>
        get() = _threadUser


    override suspend fun getThreads(threadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/Threads/$threadId/")
        val valueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                threadList.clear()
                for(child: DataSnapshot in p0.children) {
                    val thread = child.getValue(Threads :: class.java)!!
                    threadList.add(thread)
                }
                _threads.postValue(threadList)
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }


    override suspend fun getThreadsClassroom(classroomId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/classrooms/$classroomId/")
        val valueEventListener: ValueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                _threadClassroom.postValue(p0.getValue(Classroom::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }


    override fun getThreadUser(userID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/users/$userID/")
        val valueEventListener: ValueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                _threadUser.postValue(p0.getValue(User::class.java))
            }
        }
    }


    override suspend fun postThread(thread: Threads, threadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/Threads/$threadId/")
        databaseReference.push().setValue(thread)
    }


    override suspend fun postComment(comment: Comments, threadId: String, specificThreadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Threads/$threadId/$specificThreadId/comments")
        databaseReference.push().setValue(comment)
    }
}