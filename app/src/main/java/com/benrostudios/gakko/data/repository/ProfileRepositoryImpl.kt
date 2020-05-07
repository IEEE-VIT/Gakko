package com.benrostudios.gakko.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.User
import com.benrostudios.gakko.internal.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.temporal.ValueRange

class ProfileRepositoryImpl(
    private val utils: Utils
) : ProfileRepository {
    private val _response = MutableLiveData<Boolean>()
    private val _user = MutableLiveData<User>()
    var storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private lateinit var databaseReference: DatabaseReference
    private var newImageLink: String = "null"


    override val response: LiveData<Boolean>
        get() = _response
    override val user: LiveData<User>
        get() = _user


    override suspend fun uploadProfilePic(uri: Uri, person: String) {
        val profilePicUploader = storageReference.child("dp/$person/dp.jpg")
        profilePicUploader.putFile(uri).addOnCompleteListener{
            if(it.isSuccessful && it.isComplete){
                profilePicUploader.downloadUrl.addOnCompleteListener {task ->
                    utils.saveProfilePicUrl(task.result.toString())
                    newImageLink = task.result.toString()
                }
            }
        }
    }

    override suspend fun getUser(person: String) {
        databaseReference = Firebase.database.getReference("/users/$person")
        val userProfileFetcher = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var userProfile = p0.getValue(User::class.java)
                _user.postValue(userProfile)
            }

        }
        databaseReference.addListenerForSingleValueEvent(userProfileFetcher)
    }

    override suspend fun updateProfile(name: String, person: String, displayName: String) {
        databaseReference = Firebase.database.getReference("/users/$person")
        databaseReference.child("name").setValue(name)
        databaseReference.child("displayName").setValue(displayName)
        databaseReference.child("profileImage").setValue(newImageLink)
    }
}