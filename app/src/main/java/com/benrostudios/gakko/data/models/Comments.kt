package com.benrostudios.gakko.data.models

data class Comments(
    val body: String = "",
    val timestamp: Long = 0L,
    val user: Long = 0L
)