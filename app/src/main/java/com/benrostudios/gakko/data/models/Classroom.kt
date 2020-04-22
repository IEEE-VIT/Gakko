package com.benrostudios.gakko.data.models

data class Classroom (
    val chatId: String,
    val classsroomId: String,
    val createdBy: String,
    val name: String,
    val pinboard: String,
    val students: List<String>,
    val teachers: List<String>
)