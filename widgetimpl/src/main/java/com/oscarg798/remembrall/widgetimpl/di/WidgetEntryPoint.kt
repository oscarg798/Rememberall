package com.oscarg798.remembrall.widgetimpl.di

import com.oscarg798.remembrall.navigation.Navigator
import com.oscarg798.remembrall.widget.RemembrallWidgetUpdater
import com.oscarg798.remembrall.widgetimpl.navigation.WidgetNavigator
import com.oscarg798.remembrall.widgetimpl.RemembrallWidgetViewModel
import com.oscarg798.remembrall.widgetimpl.navigation.WidgetNavigatorQualifier
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetEntryPoint {

    fun viewModel(): RemembrallWidgetViewModel

    fun widgetUpdater(): RemembrallWidgetUpdater

    @WidgetNavigatorQualifier fun widgetNavigator(): Navigator
}