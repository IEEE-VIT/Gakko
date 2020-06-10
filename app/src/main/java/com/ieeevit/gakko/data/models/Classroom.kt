package com.ieeevit.gakko.data.models

data class Classroom (
    val classroomID: String,
    val courseCode: String,
    val createdBy: String,
    val name: String,
    val pinboard: String,
    val privacy: Boolean ,
    val students: List<String>,
    val teachers: List<String>,
    val requests: List<Map<String,String>>
){
    constructor(): this("","",  "","","",false,
        emptyList(),
        emptyList(),
        emptyList())
}
