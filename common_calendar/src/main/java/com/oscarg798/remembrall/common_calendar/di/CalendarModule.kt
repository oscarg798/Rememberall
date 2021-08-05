package com.oscarg798.remembrall.common_calendar.di

import com.oscarg798.remembrall.common_calendar.data.repository.CalendarRepositoryImpl
import com.oscarg798.remembrall.common_calendar.domain.repository.CalendarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CalendarModule {

    @Provides
    @Singleton
    fun provideCalendarRepository(
        calendarRepositoryImpl:
        CalendarRepositoryImpl
    ): CalendarRepository = calendarRepositoryImpl
}