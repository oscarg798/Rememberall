package com.oscarg798.remembrall.dateimpl.di

import com.oscarg798.remembrall.dateformatter.DueDateFormatter
import com.oscarg798.remembrall.dateimpl.DueDateFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DueDateFormatterBinding {

    @Binds
    fun bindDueDateFormatter(impl: DueDateFormatterImpl): DueDateFormatter
}