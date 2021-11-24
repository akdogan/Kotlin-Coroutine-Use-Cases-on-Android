package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() = runBlocking {

    val startTime = System.currentTimeMillis()

    val callList = listOf(1, 2, 3)
    val resultList = mutableListOf<Deferred<String>>()

    callList.forEach { callerId ->
        resultList.add(
            async {
                networkCall(callerId)
            }
        )
    }

    resultList.awaitAll().forEach {
        println("result received: $it. After ${elapsedMillis(startTime)} ms")
    }
}

suspend fun networkCall(number: Int): String {
    delay(500)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime