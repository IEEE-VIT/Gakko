package com.benrostudios.gakko.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val response = MutableLiveData<Boolean>()

    override suspend fun firebaseCreateWithEmailPassword(
        email: String,
        password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    response.postValue(true)
                } else {
                    response.postValue(false)
                }
                Log.d("Login", "It says: " + it.isSuccessful + "Response is: " + response)
            }
    }

    override suspend fun firebaseSignInWithEmailPassword(
        email: String,
        password: String
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    response.postValue(true)
                } else {
                    response.postValue(false)
                }
            }
    }

    override suspend fun firebaseCreateWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    response.postValue(true)
                } else {
                    response.postValue(false)
                }
            }
    }

    override fun getAuthStatus(): LiveData<Boolean> {
        return response
    }

}