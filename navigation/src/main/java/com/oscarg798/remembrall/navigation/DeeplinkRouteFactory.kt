package com.oscarg798.remembrall.navigation

import android.net.Uri
import android.os.Bundle

interface DeeplinkRouteFactory : (Uri, Bundle?) -> Uri

