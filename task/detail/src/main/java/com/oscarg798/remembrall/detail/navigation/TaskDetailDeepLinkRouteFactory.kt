package com.oscarg798.remembrall.detail.navigation

import android.net.Uri
import android.os.Bundle
import com.oscarg798.remembrall.navigation.DeepLinkUriBuilder
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import com.oscarg798.remembrall.navigation.Route
import javax.inject.Inject

class TaskDetailDeepLinkRouteFactory @Inject constructor(
    private val deepLinkUriBuilder: DeepLinkUriBuilder
) : DeeplinkRouteFactory {

    override fun invoke(uriPattern: Uri, arguments: Bundle?): Uri {
        val uriPath = uriPattern.path
        require(uriPath != null)

        val newPath = uriPath.replace(
            "{${Route.TaskIdArgument}}",
            arguments?.getString(
                Route.TaskIdArgument
            ) ?: Route.NO_PARAMETER
        )

        return deepLinkUriBuilder.appendPath(newPath)
    }
}