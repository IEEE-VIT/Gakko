package com.benrostudios.gakko.data.repository

import android.util.Log
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.gakko.data.models.ChatMessage
import com.benrostudios.gakko.internal.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRepositoryImpl(
    private val utils: Utils
) : ChatRepository {

    private val userPhoneNumber = utils.retrieveMobile()
    private val currentClassroom = utils.retrieveCurrentClassroom()
    private val currentChat = utils.retrieveCurrentChat()
    private var usrChats = mutableListOf<ChatMessage>()
    private lateinit var chatLink: String
    private var _usrChatMessages = MutableLiveData<List<ChatMessage>>()
    private lateinit var databaseReference: DatabaseReference

    override val chatMessages: LiveData<List<ChatMessage>>
        get() = _usrChatMessages

    override suspend fun sendMessage(message: ChatMessage) {
        formLink()
        databaseReference = Firebase.database.getReference("/chats/$currentClassroom/$chatLink")
        databaseReference.push().setValue(message)
    }

    override suspend fun receiveMessage() {
        formLink()
        usrChats = mutableListOf<ChatMessage>()
        databaseReference = Firebase.database.getReference("/chats/$currentClassroom/$chatLink")
        var chatReceiver = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for(messages in p0.children){
                    var chatMessage = messages.getValue(ChatMessage::class.java)
                    usrChats.add(chatMessage!!)
                    _usrChatMessages.postValue(usrChats)
                }
            }
        }
        databaseReference.addValueEventListener(chatReceiver)
    }

    private fun formLink(){
        var userNumber = utils.retrieveMobile()?.slice(3..12)?.toLongOrNull() ?: 0
        var chatNumber = utils.retrieveCurrentChat()?.slice(3..12)?.toLongOrNull() ?: 1
        chatLink = "${minOf(userNumber,chatNumber)}-${maxOf(userNumber,chatNumber)}"
        Log.d("formLink",chatLink)
    }
}