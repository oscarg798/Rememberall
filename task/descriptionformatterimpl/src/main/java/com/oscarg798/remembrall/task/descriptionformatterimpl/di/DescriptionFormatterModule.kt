package com.oscarg798.remembrall.task.descriptionformatterimpl.di

import com.oscarg798.rememberall.task.descriptionformatter.DescriptionFormatter
import com.oscarg798.remembrall.task.descriptionformatterimpl.DescriptionFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DescriptionFormatterModule {

    @Binds
    fun bindDescriptionFormatter(impl: DescriptionFormatterImpl): DescriptionFormatter
}