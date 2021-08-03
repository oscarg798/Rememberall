package com.oscarg798.remembrall.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import androidx.room.Room
import androidx.work.WorkManager
import com.google.gson.Gson
import com.oscarg798.remebrall.common_calendar.data.repository.CalendarRepositoryImpl
import com.oscarg798.remebrall.common_calendar.domain.repository.CalendarRepository
import com.oscarg798.remebrall.schedule.util.PendingIntentFinder
import com.oscarg798.remembrall.BuildConfig
import com.oscarg798.remembrall.common.HomeActivityPendingIntentFinder
import com.oscarg798.remembrall.common.coroutines.CoroutineContextProvider
import com.oscarg798.remembrall.common.datasource.TaskDataSource
import com.oscarg798.remembrall.common.persistence.AppDatabase
import com.oscarg798.remembrall.common.persistence.LocalDataSource
import com.oscarg798.remembrall.common.persistence.TaskDao
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.provider.StringProviderImpl
import com.oscarg798.remembrall.common.repository.data.LocalPreferenceRepository
import com.oscarg798.remembrall.common.repository.data.TaskRepositoryImpl
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import com.oscarg798.remembrall.common.repository.domain.TaskRepository
import com.oscarg798.remembrall.common_auth.model.AuthOptions
import com.oscarg798.remembrall.common_auth.network.restclient.ExternalSignInClient
import com.oscarg798.remembrall.common_auth.network.restclient.GoogleSignInClient
import com.oscarg798.remembrall.common_auth.repository.data.GoogleAuthRepository
import com.oscarg798.remembrall.common_auth.repository.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.regex.Pattern
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideCoroutinesContextProvider() =
        CoroutineContextProvider(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )

    @Provides
    @Singleton
    fun provideStringProvider(stringProviderImpl: StringProviderImpl): StringProvider =
        stringProviderImpl

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java, DatabaseName
    ).build()

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao = appDatabase.taskDao()

    @Provides
    @Singleton
    fun provideTaskDataSource(
        localDataSource:
            LocalDataSource
    ): TaskDataSource = localDataSource

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDataSource:
            TaskDataSource
    ): TaskRepository =
        TaskRepositoryImpl(taskDataSource)

    @Provides
    @Reusable
    fun provideGoogleAuthRepository(
        googleSignInRepositoryImpl:
            GoogleAuthRepository
    ): AuthRepository =
        googleSignInRepositoryImpl

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PreferenceName, Private)
    }

    @Provides
    @Singleton
    fun provideCalendarRepository(
        calendarRepositoryImpl:
            CalendarRepositoryImpl
    ): CalendarRepository =
        calendarRepositoryImpl

    @Provides
    @Singleton
    fun provideAuthOptions(): AuthOptions = AuthOptions(
        requestServerAuth = true,
        requestProfileInfo = true,
        scopes = setOf(CalendarScope),
        clientId = BuildConfig.GoogleClientId
    )

    @Provides
    @Singleton
    fun provideExternalSignInClient(
        googleSignInClient:
            GoogleSignInClient
    ): ExternalSignInClient =
        googleSignInClient

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providePreferenceRepository(
        localPreferenceRepository:
            LocalPreferenceRepository
    ): PreferenceRepository =
        localPreferenceRepository

    @Provides
    @Reusable
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Reusable
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Reusable
    fun provideEmailPattern(): Pattern = Patterns.EMAIL_ADDRESS

    @Provides
    @Reusable
    fun providePendingIntentFinder(
        homeActivityPendingIntentFinder:
            HomeActivityPendingIntentFinder
    ): PendingIntentFinder =
        homeActivityPendingIntentFinder
}

private const val Private = 0
private const val PreferenceName = "Remembrall"
private const val DatabaseName = "Remembrall"
private const val CalendarScope = "https://www.googleapis.com/auth/calendar"
