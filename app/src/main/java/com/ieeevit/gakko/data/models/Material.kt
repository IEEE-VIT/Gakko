package com.ieeevit.gakko.data.models

data class Material(
    val attachmentType: String = "",
    val deadline: Long = 0L,
    val name: String = "",
    val type: String = "",
    val uploadedBy: String = "",
    val uploadedOn: Long = 0L,
    val url: String = "",
    val classroomName: String = ""
)