package com.oscarg798.remembrall.calendarimpl.di

import com.oscarg798.remembrall.calendarimpl.CalendarRepositoryImpl
import com.remembrall.oscarg798.calendar.CalendarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface CalendarBindingsModule {

    @Binds
    fun bindCalendarRepository(impl: CalendarRepositoryImpl): CalendarRepository
}
