package com.xp090.azemaattendance.util.extra

import kotlinx.coroutines.*


suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return GlobalScope.async(Dispatchers.Default) { block() }
}suspend fun <T> asyncAwait(block: suspend CoroutineScope.() -> T): T {
    return async(block).await()
}

fun launchAsync(block: suspend CoroutineScope.() -> Unit): Job {
    return GlobalScope.launch(Dispatchers.Main) { block() }
}