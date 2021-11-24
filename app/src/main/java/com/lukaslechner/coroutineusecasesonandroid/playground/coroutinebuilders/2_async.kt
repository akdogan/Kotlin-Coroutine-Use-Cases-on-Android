package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val startTime = System.currentTimeMillis()

    launch {
        val result1 = networkCall(1)
        println("result received: $result1 after ${elapsedMillis(startTime)} ms")
    }
    launch {
        val result2 = networkCall(2)
        println("result received: $result2 after ${elapsedMillis(startTime)} ms")
    }
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime