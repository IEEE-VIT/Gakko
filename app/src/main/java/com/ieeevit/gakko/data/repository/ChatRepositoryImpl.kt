package com.ieeevit.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ieeevit.gakko.data.models.ChatMessage
import com.ieeevit.gakko.data.models.User
import com.ieeevit.gakko.internal.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRepositoryImpl(
    private val utils: Utils
) : ChatRepository {

    private var currentClassroom = utils.retrieveCurrentClassroom()
    private var usrChats = mutableListOf<ChatMessage>()
    private var _recipientUser = MutableLiveData<User>()
    private lateinit var chatLink: String
    private var _usrChatMessages = MutableLiveData<List<ChatMessage>>()
    private lateinit var databaseReference: DatabaseReference

    override val recipientUser: LiveData<User>
        get() = _recipientUser

    override val chatMessages: LiveData<List<ChatMessage>>
        get() = _usrChatMessages

    override suspend fun sendMessage(message: ChatMessage) {
        formLink()
        databaseReference = Firebase.database.getReference("/chats/$currentClassroom/$chatLink")
        databaseReference.push().setValue(message)
    }

    override suspend fun receiveMessage() {
        currentClassroom = utils.retrieveCurrentClassroom()
        formLink()
        fetchRecipientUser()
        databaseReference = Firebase.database.getReference("/chats/$currentClassroom/$chatLink")
        var chatReceiver = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                Log.d("From Chat","$currentClassroom")
                usrChats = mutableListOf<ChatMessage>()
                if(p0.exists()) {
                    for (messages in p0.children) {
                        var chatMessage = messages.getValue(ChatMessage::class.java)
                        usrChats.add(chatMessage!!)
                        _usrChatMessages.postValue(usrChats)
                    }
                }else{
                    _usrChatMessages.postValue(emptyList())
                }
            }
        }
        databaseReference.addValueEventListener(chatReceiver)
    }

    override suspend fun resetChat() {
        _usrChatMessages.postValue(emptyList())
    }

    private fun formLink(){
        var userNumber = utils.retrieveMobile()?.slice(3..12)?.toLongOrNull() ?: 0
        var chatNumber = utils.retrieveCurrentChat()?.slice(3..12)?.toLongOrNull() ?: 1
        chatLink = "${minOf(userNumber,chatNumber)}-${maxOf(userNumber,chatNumber)}"
        Log.d("formLink",chatLink)
    }

    fun fetchRecipientUser(){
        databaseReference = Firebase.database.getReference("/users/${utils.retrieveCurrentChat()}")
        var recipientFetcher = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
               var recipientUser = p0.getValue(User::class.java)
                _recipientUser.postValue(recipientUser)
            }

        }
        databaseReference.addListenerForSingleValueEvent(recipientFetcher)
    }
}