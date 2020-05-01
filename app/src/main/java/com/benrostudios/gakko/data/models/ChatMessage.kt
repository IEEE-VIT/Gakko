package com.benrostudios.gakko.data.models

data class ChatMessage(
    val attachmentType: String,
    val attachmentUrl: String ,
    val body: String,
    val classroomId: String ,
    val recipient: String ,
    val seen: Boolean,
    val sender: String ,
    val sent: Boolean,
    val timestamp: Long
){
    constructor():this("","","","","",false,"",false,0)
}