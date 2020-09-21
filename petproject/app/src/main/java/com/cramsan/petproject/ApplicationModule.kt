package com.cramsan.petproject

import android.content.Context
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.cramsan.framework.crashehandler.CrashHandlerInterface
import com.cramsan.framework.crashehandler.implementation.AppCenterCrashHandler
import com.cramsan.framework.crashehandler.implementation.CrashHandler
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.HaltUtilInterface
import com.cramsan.framework.halt.implementation.HaltUtil
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLogger
import com.cramsan.framework.logging.implementation.LoggerAndroid
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.AppCenterMetrics
import com.cramsan.framework.metrics.implementation.Metrics
import com.cramsan.framework.metrics.implementation.MetricsErrorCallback
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.PreferencesInterface
import com.cramsan.framework.preferences.implementation.Preferences
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtil
import com.cramsan.framework.thread.implementation.ThreadUtilAndroid
import com.cramsan.petproject.appcore.provider.ModelProviderInterface
import com.cramsan.petproject.appcore.provider.ProviderConfig
import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.ModelStorageInterface
import com.cramsan.petproject.appcore.storage.ModelStoragePlatformProvider
import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageAndroidProvider
import com.cramsan.petproject.work.DailySyncManager
import com.cramsan.petproject.work.ScheduledSyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    /*
    WTF: I don't know why this is not working
    @Provides
    fun provideAssertUtilInterface(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtilInterface
    ): AssertUtilInterface {
        return AssertUtil(
            BuildConfig.DEBUG,
            eventLoggerInterface,
            haltUtilInterface
        )
    }
     */

    @Provides
    @Singleton
    fun provideThreadUtilDelegate(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtilInterface
    ): ThreadUtilDelegate {
        return ThreadUtilAndroid(
            AssertUtil(
                BuildConfig.DEBUG,
                eventLoggerInterface,
                haltUtilInterface
            )
        )
    }

    @Provides
    @Singleton
    fun provideCrashHandlerDelegate(): CrashHandlerDelegate = AppCenterCrashHandler()

    @Provides
    @Singleton
    fun provideCrashHandlerInterface(crashHandlerDelegate: CrashHandlerDelegate): CrashHandlerInterface =
        CrashHandler(crashHandlerDelegate)

    @Provides
    @Singleton
    fun provideMetricsDelegate(): MetricsDelegate = AppCenterMetrics()

    @Provides
    @Singleton
    fun provideMetricsInterface(metricsDelegate: MetricsDelegate): MetricsInterface =
        Metrics(metricsDelegate)

    @Provides
    @Singleton
    fun provideEventLoggerErrorCallbackInterface(metricsInterface: MetricsInterface): EventLoggerErrorCallbackInterface =
        MetricsErrorCallback(metricsInterface)

    @Provides
    @Singleton
    fun provideEventLoggerDelegate(): EventLoggerDelegate = LoggerAndroid()

    @Provides
    @Singleton
    fun provideEventLoggerInterface(
        eventLoggerErrorCallbackInterface: EventLoggerErrorCallbackInterface,
        eventLoggerDelegate: EventLoggerDelegate
    ): EventLoggerInterface {
        val severity: Severity = when (BuildConfig.DEBUG) {
            true -> Severity.DEBUG
            false -> Severity.INFO
        }
        return EventLogger(severity, eventLoggerErrorCallbackInterface, eventLoggerDelegate)
    }

    @Provides
    @Singleton
    fun provideHaltUtilDelegate(): HaltUtilDelegate = HaltUtilAndroid()

    @Provides
    @Singleton
    fun provideHaltUtilInterface(haltUtilDelegate: HaltUtilDelegate): HaltUtilInterface =
        HaltUtil(haltUtilDelegate)

    @Provides
    @Singleton
    fun provideThreadUtilInterface(threadUtilDelegate: ThreadUtilDelegate): ThreadUtilInterface =
        ThreadUtil(threadUtilDelegate)

    @Provides
    @Singleton
    fun provideModelStoragePlatformProvider(@ApplicationContext appContext: Context): ModelStoragePlatformProvider =
        ModelStorageAndroidProvider(appContext)

    @Provides
    @Singleton
    fun provideModelStorageDAO(modelStoragePlatformProvider: ModelStoragePlatformProvider): ModelStorageDAO =
        modelStoragePlatformProvider.provide()

    @Provides
    @Singleton
    fun provideModelStorageInterface(
        modelStorageDAO: ModelStorageDAO,
        eventLoggerInterface: EventLoggerInterface,
        threadUtilInterface: ThreadUtilInterface
    ): ModelStorageInterface =
        ModelStorage(
            modelStorageDAO,
            eventLoggerInterface,
            threadUtilInterface
        )

    @Provides
    @Singleton
    fun providePreferencesDelegate(@ApplicationContext appContext: Context): PreferencesDelegate =
        PreferencesAndroid(
            appContext
        )

    @Provides
    @Singleton
    fun providePreferencesInterface(preferencesDelegate: PreferencesDelegate): PreferencesInterface =
        Preferences(preferencesDelegate)

    @Provides
    @Singleton
    fun provideProviderConfig(@ApplicationContext appContext: Context): ProviderConfig {
        return ProviderConfig(
            appContext.getString(R.string.provider_config_plants_url),
            appContext.getString(R.string.provider_config_mainname_url),
            appContext.getString(R.string.provider_config_commonname_url),
            appContext.getString(R.string.provider_config_description_url),
            appContext.getString(R.string.provider_config_family_url),
            appContext.getString(R.string.provider_config_toxicities_url)
        )
    }

    @Provides
    @Singleton
    fun provideModelProviderInterface(
        eventLoggerInterface: EventLoggerInterface,
        threadUtilInterface: ThreadUtilInterface,
        modelStorageInterface: ModelStorageInterface,
        preferencesInterface: PreferencesInterface,
        providerConfig: ProviderConfig,
    ): ModelProviderInterface = ModelProvider(
        eventLoggerInterface,
        threadUtilInterface,
        modelStorageInterface,
        preferencesInterface,
        providerConfig
    )

    @Provides
    @Singleton
    fun provideScheduledSyncManager(@ApplicationContext appContext: Context): ScheduledSyncManager =
        DailySyncManager(appContext)

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}
