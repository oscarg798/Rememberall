package com.oscarg798.remembrall.di

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Patterns
import androidx.work.WorkManager
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.oscarg798.remebrall.coroutinesutils.CoroutineContextProvider
import com.oscarg798.remembrall.BuildConfig
import com.oscarg798.remembrall.common.HomeActivityPendingIntentFinder
import com.oscarg798.remembrall.common.IdentifierGenerator
import com.oscarg798.remembrall.common.provider.StringProvider
import com.oscarg798.remembrall.common.provider.StringProviderImpl
import com.oscarg798.remembrall.common.repository.data.LocalPreferenceRepository
import com.oscarg798.remembrall.common.repository.domain.PreferenceRepository
import com.oscarg798.remembrall.config.Config
import com.oscarg798.remembrall.rxutils.SchedulersProvider
import com.oscarg798.remembrall.schedule.util.PendingIntentFinder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.UUID
import java.util.regex.Pattern
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx3.asScheduler

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideCoroutinesContextProvider() = object : CoroutineContextProvider {
        override val io: CoroutineContext
            get() = Dispatchers.IO
        override val computation: CoroutineContext
            get() = Dispatchers.Default
        override val main: CoroutineContext
            get() = Dispatchers.Main
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider() = object : SchedulersProvider {
        override val io: Scheduler
            get() = Schedulers.io()
        override val computation: Scheduler
            get() = Schedulers.computation()
        override val main: Scheduler
            get() = Dispatchers.Main.asScheduler()
    }

    @Provides
    @Singleton
    fun provideStringProvider(stringProviderImpl: StringProviderImpl): StringProvider =
        stringProviderImpl


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PreferenceName, Context.MODE_PRIVATE)
    }

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

    @ExperimentalPagerApi
    @Provides
    @Reusable
    fun providePendingIntentFinder(
        homeActivityPendingIntentFinder:
            HomeActivityPendingIntentFinder
    ): PendingIntentFinder =
        homeActivityPendingIntentFinder

    @Provides
    @Reusable
    fun provideConfig(): Config = Config(
        clientId = BuildConfig.GoogleClientId,
        clientSecret = BuildConfig.GoogleClientSecret
    )

    @Singleton
    @Provides
    fun provideFirebaseStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideIdentifierGenerator(): IdentifierGenerator = object : IdentifierGenerator {
        override fun createStringIdentifier(): String = UUID.randomUUID().toString()
    }
}

private const val PreferenceName = "Remembrall"
