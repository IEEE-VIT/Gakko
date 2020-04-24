package com.benrostudios.gakko.data.models

data class Threads(
    val body: String = "",
    val comments: List<Comments> = emptyList(),
    val user: Long = 0L
)
