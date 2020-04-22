package com.benrostudios.gakko.data.models

data class User(
    val classrooms: List<Classroom>?,
    val displayName: String,
    val id: String,
    val name: String,
    val online: Boolean,
    val profileImage: String
)