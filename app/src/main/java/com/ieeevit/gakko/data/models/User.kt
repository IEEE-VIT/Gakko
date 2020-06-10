package com.ieeevit.gakko.data.models

data class User(
    val classrooms: List<String>?,
    val displayName: String,
    val id: String,
    val name: String,
    val online: Boolean,
    val profileImage: String
){
    constructor():this(emptyList(),"","","",false,"")
}