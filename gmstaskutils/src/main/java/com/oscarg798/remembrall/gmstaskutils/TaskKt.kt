package com.oscarg798.remembrall.gmstaskutils

import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun <T> Task<T>.toSuspend(
    errorWrapper: (Exception?) -> Exception = {
        it ?: RuntimeException()
    }
) = suspendCancellableCoroutine<Task<T>> { continuation ->
        addOnCompleteListener { taskResult ->
            if (!continuation.isActive) {
                return@addOnCompleteListener
            }

            if (!taskResult.isSuccessful) {
                continuation.resumeWithException(errorWrapper(taskResult.exception))
                return@addOnCompleteListener
            }

            continuation.resume(taskResult)
        }
    }