package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthRepositoryImpl :
    AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val AUTH_REPO_TAG = "AuthRepo"
    private lateinit var firebaseDatabase: DatabaseReference
    private val _response = MutableLiveData<Boolean>()
    private val _userResponse = MutableLiveData<Boolean>()

    override suspend fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(AUTH_REPO_TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    _response.postValue(true)
                } else {
                    Log.w(AUTH_REPO_TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        _response.postValue(false)
                    }
                }
            }
    }

    override val getAuthStatus: LiveData<Boolean>
        get() = _response


    override fun checkUser(phone: String) {
        firebaseDatabase = Firebase.database.getReference("/users/$phone")
        val userChecker = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.exists()){
                    _userResponse.postValue(false)
                }else{
                    _userResponse.postValue(true)
                }
            }
        }
        firebaseDatabase.addListenerForSingleValueEvent(userChecker)
    }

    override val userResponse: LiveData<Boolean>
        get() = _userResponse

    override suspend fun registerUser(user: User) {
        firebaseDatabase = Firebase.database.getReference("/users")
        firebaseDatabase.child(user.id).setValue(user)
    }

}