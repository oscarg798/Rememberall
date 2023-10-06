package com.oscarg798.remembrall.navigationutils

import com.oscarg798.remembrall.navigation.Route
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class DeeplinkRouteFactoryKey(val value: KClass<out Route>)