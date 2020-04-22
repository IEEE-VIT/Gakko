package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FirebaseRepositoryImpl : FirebaseRepository {
    private lateinit var firebaseDatabase:DatabaseReference
    private val _response = MutableLiveData<Boolean>()




    override fun checkUser(phone: String) {
        firebaseDatabase = Firebase.database.getReference("/users/" + phone)
        val userChecker = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.exists()){
                    _response.postValue(false)
                }else{
                    _response.postValue(true)
                }
                Log.d("Hello", p0.toString())
            }
        }
        firebaseDatabase.addListenerForSingleValueEvent(userChecker)
    }

    override val userResponse: LiveData<Boolean>
        get() = _response

    override suspend fun registerUser(user: User) {

    }
}