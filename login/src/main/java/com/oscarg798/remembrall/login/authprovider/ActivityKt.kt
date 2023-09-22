package com.oscarg798.remembrall.login.authprovider

import android.app.Activity
import android.content.IntentSender
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

internal suspend fun Activity.launchForResult(
    intentSender: IntentSender,
    requestCode: String
): ActivityResult = suspendCancellableCoroutine { continuation ->
    val activityResultRegistryOwner = ((this as? ActivityResultRegistryOwner))
        ?: throw IllegalStateException(
            "activityResultRegistry requires an activity as ActivityResultRegistryOwner"
        )

    val launcher = activityResultRegistryOwner.activityResultRegistry.register(
        requestCode,
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (continuation.isActive) {
            continuation.resume(it)
        }
    }

    launcher.launch(
        IntentSenderRequest.Builder(intentSender)
            .build()
    )

    continuation.invokeOnCancellation {
        launcher.unregister()
    }
}