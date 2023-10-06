package com.oscarg798.remembrall.task.addrouteprovider

import android.net.Uri
import android.os.Bundle
import com.oscarg798.remembrall.task.addroute.AddRoute.None
import com.oscarg798.remembrall.task.addroute.AddRoute.TaskIdArgument
import com.oscarg798.remembrall.navigation.DeepLinkUriBuilder
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory
import javax.inject.Inject

internal class AddDeeplinkRouteFactory @Inject constructor(
    private val deepLinkUriBuilder: DeepLinkUriBuilder
) : DeeplinkRouteFactory {

    override fun invoke(uriPattern: Uri, arguments: Bundle?): Uri {
        val uriPath = uriPattern.path
        require(uriPath != null)

        val newPath = uriPath.replace(
            "{$TaskIdArgument}",
            arguments?.getString(
                TaskIdArgument
            ) ?: None
        )

        return deepLinkUriBuilder.appendPath(newPath)
    }
}