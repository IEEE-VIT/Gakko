package com.benrostudios.gakko.data.models

data class ClassroomJoinRequest(
    var classroomId: String,
    var name: String,
    var phone: String,
    var timestamp: Long,
    var profileImageLink: String,
    var classroomName: String
)