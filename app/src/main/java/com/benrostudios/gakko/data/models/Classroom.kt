package com.benrostudios.gakko.data.models

data class Classroom (
    val chatId: String,
    val classroomId: String,
    val createdBy: String,
    val name: String,
    val pinboard: String,
    val privacy: String ,
    val slots: String,
    val students: List<String>,
    val teachers: List<String>,
    val requests: Map<String,String>
){
    constructor(): this("","","","","","","",
        emptyList(),
        emptyList(),
        emptyMap())
}
