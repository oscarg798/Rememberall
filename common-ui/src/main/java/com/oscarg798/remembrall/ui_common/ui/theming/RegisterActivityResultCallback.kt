package com.oscarg798.remembrall.ui_common.ui.theming

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityOptionsCompat
import java.util.UUID

@Composable
fun <Input, Output> registerActivityResultCallback(
    contract: ActivityResultContract<Input, Output>,
    onResult: (Output) -> Unit
): ActivityResultLauncher<Input> {

    val owner = LocalContext.current as ActivityResultRegistryOwner
    val activityResultRegistry = owner.activityResultRegistry

    val currentOnResult = rememberUpdatedState(onResult)

    val key = rememberSaveable { UUID.randomUUID().toString() }

    val realLauncher = remember { mutableStateOf<ActivityResultLauncher<Input>?>(null) }

    val returnedLauncher = remember {
        object : ActivityResultLauncher<Input>() {
            override fun launch(input: Input, options: ActivityOptionsCompat?) {
                realLauncher.value?.launch(input, options)
            }

            override fun unregister() {
                realLauncher.value?.unregister()
            }

            override fun getContract() = contract
        }
    }

    DisposableEffect(activityResultRegistry, key, contract) {
        realLauncher.value = activityResultRegistry.register(key, contract) {
            currentOnResult.value(it)
        }

        onDispose {
            realLauncher.value?.unregister()
        }
    }
    return returnedLauncher
}
