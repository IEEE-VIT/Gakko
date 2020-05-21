package com.benrostudios.gakko.internal

import java.util.*

class RandomColors {
    private val recycle: Stack<Int> = Stack()
    private val colors: Stack<Int> = Stack()

    fun getColor(): Int {
        if (colors.size === 0) {
            while (!recycle.isEmpty()) colors.push(recycle.pop())
            Collections.shuffle(colors)
        }
        val c: Int = colors.pop()
        recycle.push(c)
        return c
    }

    init {
        recycle.addAll(
            listOf(-0xfff600)
        )
    }
}