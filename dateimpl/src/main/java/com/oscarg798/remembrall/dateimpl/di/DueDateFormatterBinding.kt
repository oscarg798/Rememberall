package com.oscarg798.remembrall.dateimpl.di

import com.oscarg798.remembrall.dateformatter.DateFormatter
import com.oscarg798.remembrall.dateimpl.DateFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DueDateFormatterBinding {

    @Binds
    fun bindDueDateFormatter(impl: DateFormatterImpl): DateFormatter
}
