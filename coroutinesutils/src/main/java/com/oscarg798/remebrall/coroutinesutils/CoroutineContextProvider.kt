package com.oscarg798.remebrall.coroutinesutils

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val io: CoroutineContext
    val computation: CoroutineContext
    val main: CoroutineContext
}