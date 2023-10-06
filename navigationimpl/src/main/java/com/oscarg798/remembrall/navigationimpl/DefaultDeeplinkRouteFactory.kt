package com.oscarg798.remembrall.navigationimpl

import android.net.Uri
import android.os.Bundle
import com.oscarg798.remembrall.navigation.DeeplinkRouteFactory

internal object DefaultDeeplinkRouteFactory : DeeplinkRouteFactory {

    override fun invoke(uri: Uri, arguments: Bundle?): Uri = uri
}