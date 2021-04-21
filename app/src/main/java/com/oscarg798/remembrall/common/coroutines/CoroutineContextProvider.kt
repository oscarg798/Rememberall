package com.oscarg798.remembrall.common.coroutines

import kotlin.coroutines.CoroutineContext

class CoroutineContextProvider(
    val io: CoroutineContext,
    val computation: CoroutineContext,
    val main: CoroutineContext
)
