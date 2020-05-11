package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.*

class CommentsRepositoryImpl : CommentsRepository {

    private val _specificThread = MutableLiveData<Threads>()
    private val _commentUser = MutableLiveData<User>()
    private val _threadUser = MutableLiveData<User>()
    private lateinit var databaseReference: DatabaseReference


    override val specificThread: LiveData<Threads>
        get() = _specificThread
    override val commentUser: LiveData<User>
        get() = _commentUser
    override val threadUser: LiveData<User>
        get() = _threadUser


    override suspend fun getCommentUser(userID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/users/$userID/")
        val valueEventListener: ValueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                _commentUser.postValue(p0.getValue(User::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }

    override suspend fun getThreadUser(userID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/users/$userID/")
        val valueEventListener: ValueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                _threadUser.postValue(p0.getValue(User::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }

    override suspend fun getSpecificThread(threadId: String, specificThreadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/Threads/$threadId/$specificThreadId/")
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                _specificThread.postValue(p0.getValue(Threads::class.java))
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }

    override suspend fun postComment(comment: Comments, threadId: String, specificThreadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Threads/$threadId/$specificThreadId/comments")
        databaseReference.push().setValue(comment)
    }
}