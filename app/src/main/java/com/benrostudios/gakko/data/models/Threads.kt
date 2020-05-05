package com.benrostudios.gakko.data.models

data class Threads(
    val body: String = "",
    val comments: Map<String, Comments> = emptyMap(),
    val user: String = "",
    val timestamp: Long = 0L,
    var threadId: String = ""
)
