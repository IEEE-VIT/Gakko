package com.benrostudios.gakko.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.Comments
import com.benrostudios.gakko.data.models.Threads
import com.google.firebase.database.*

class ThreadsRepositoryImpl : ThreadsRepository {
    private val _threads = MutableLiveData<List<Threads>>()
    private lateinit var databaseReference: DatabaseReference

    override val threads: LiveData<List<Threads>>
        get() = _threads

    override suspend fun getThreads(threadId: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("/Threads/$threadId/")

        val valueEventListener = object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                lateinit var threads: MutableList<Threads>
                for(child: DataSnapshot in p0.children) {
                    threads.add(child.value as Threads)
                }
                _threads.postValue(threads)
                threads.clear()
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
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