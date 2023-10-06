package com.oscarg798.remembrall.task.addroute

import android.net.Uri
import androidx.core.net.toUri
import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.navigation.Route

object AddRoute : Route {
    override val route: String
        get() = AddTaskRoute

    override val uriPattern: Uri by lazy {
        "${Navigator.DeepLinkUri}/$AddTaskRoute/{$TaskIdArgument}".toUri()
    }

    const val TaskIdArgument = "TaskId"
    const val None = "none"

}

private const val AddTaskRoute = "addTask"


