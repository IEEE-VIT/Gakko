package com.benrostudios.gakko.data.models

data class Threads(
    val body: String,
    val comments: List<Comments>,
    val user: String
)
