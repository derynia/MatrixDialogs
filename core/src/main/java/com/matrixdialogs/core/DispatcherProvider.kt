package com.matrixdialogs.core

import kotlinx.coroutines.CoroutineDispatcher
 /*
 * Dispatcher provider interface usable for mocking coroutine dispatchers while testing
 */

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}