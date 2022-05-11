package com.cramsan.petproject

import android.content.Context
import com.cramsan.framework.assertlib.AssertUtil
import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.assertlib.implementation.AssertUtilImpl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.DispatcherProviderImpl
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.cramsan.framework.crashehandler.implementation.AppCenterCrashHandler
import com.cramsan.framework.crashehandler.implementation.AppCenterErrorCallback
import com.cramsan.framework.crashehandler.implementation.CrashHandlerImpl
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallback
import com.cramsan.framework.logging.EventLoggerErrorCallbackDelegate
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerErrorCallbackImpl
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerAndroid
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.preferences.implementation.PreferencesImpl
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilAndroid
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.framework.userevents.UserEvents
import com.cramsan.framework.userevents.UserEventsDelegate
import com.cramsan.framework.userevents.UserEventsInterface
import com.cramsan.framework.userevents.implementation.AppCenterUserEvents
import com.cramsan.framework.userevents.implementation.UserEventsImpl
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
@Suppress("TooManyFunctions", "UndocumentedPublicFunction")
object PetProjectApplicationModule {

    @Provides
    @Singleton
    fun provideAssertUtil(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): AssertUtilInterface {
        val impl = AssertUtilImpl(
            BuildConfig.DEBUG,
            eventLoggerInterface,
            haltUtilInterface,
        )
        AssertUtil.setInstance(impl)
        return AssertUtil.singleton
    }

    @Provides
    @Singleton
    fun provideThreadUtilDelegate(
        assertUtilInterface: AssertUtilInterface,
    ): ThreadUtilDelegate {
        return ThreadUtilAndroid(assertUtilInterface)
    }

    @Provides
    @Singleton
    fun provideCrashHandlerDelegate(): CrashHandlerDelegate = AppCenterCrashHandler()

    @Provides
    @Singleton
    fun provideCrashHandlerInterface(crashHandlerDelegate: CrashHandlerDelegate): CrashHandler =
        CrashHandlerImpl(crashHandlerDelegate)

    @Provides
    @Singleton
    fun provideUserEventsDelegate(): UserEventsDelegate = AppCenterUserEvents()

    @Provides
    @Singleton
    fun provideUserEventsInterface(
        userEventsDelegate: UserEventsDelegate,
        eventLoggerInterface: EventLoggerInterface,
    ): UserEventsInterface {
        val instance = UserEventsImpl(userEventsDelegate, eventLoggerInterface)
        UserEvents.setInstance(instance)
        return UserEvents.singleton
    }

    @Provides
    @Singleton
    fun provideEventLoggerErrorCallbackDelegate(): EventLoggerErrorCallbackDelegate =
        AppCenterErrorCallback()

    @Provides
    @Singleton
    fun provideEventLoggerErrorCallback(
        eventLoggerDelegate: EventLoggerDelegate,
        delegate: EventLoggerErrorCallbackDelegate,
    ): EventLoggerErrorCallback =
        EventLoggerErrorCallbackImpl(eventLoggerDelegate, delegate)

    @Provides
    @Singleton
    fun provideEventLoggerDelegate(): EventLoggerDelegate = LoggerAndroid()

    @Provides
    @Singleton
    fun provideEventLoggerInterface(
        eventLoggerErrorCallbackInterface: EventLoggerErrorCallback,
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val severity: Severity = when (BuildConfig.DEBUG) {
            true -> Severity.DEBUG
            false -> Severity.INFO
        }
        val instance =
            EventLoggerImpl(severity, eventLoggerErrorCallbackInterface, eventLoggerDelegate)
        EventLogger.setInstance(instance)
        return EventLogger.singleton
    }

    @Provides
    @Singleton
    fun provideHaltUtilDelegate(@ApplicationContext appContext: Context): HaltUtilDelegate = HaltUtilAndroid(appContext)

    @Provides
    @Singleton
    fun provideHaltUtilInterface(haltUtilDelegate: HaltUtilDelegate): HaltUtil =
        HaltUtilImpl(haltUtilDelegate)

    @Provides
    @Singleton
    fun provideThreadUtilInterface(threadUtilDelegate: ThreadUtilDelegate): ThreadUtilInterface {
        val instance = ThreadUtilImpl(threadUtilDelegate)
        ThreadUtil.setInstance(instance)
        return ThreadUtil.singleton
    }

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
        threadUtilInterface: ThreadUtilInterface,
    ): ModelStorageInterface =
        ModelStorage(
            modelStorageDAO,
            eventLoggerInterface,
            threadUtilInterface,
        )

    @Provides
    @Singleton
    fun providePreferencesDelegate(@ApplicationContext appContext: Context): PreferencesDelegate =
        PreferencesAndroid(
            appContext,
        )

    @Provides
    @Singleton
    fun providePreferencesInterface(preferencesDelegate: PreferencesDelegate): Preferences =
        PreferencesImpl(preferencesDelegate)

    @Provides
    @Singleton
    fun provideProviderConfig(@ApplicationContext appContext: Context): ProviderConfig {
        return ProviderConfig(
            appContext.getString(R.string.provider_config_plants_url),
            appContext.getString(R.string.provider_config_mainname_url),
            appContext.getString(R.string.provider_config_commonname_url),
            appContext.getString(R.string.provider_config_description_url),
            appContext.getString(R.string.provider_config_family_url),
            appContext.getString(R.string.provider_config_toxicities_url),
        )
    }

    @Provides
    @Singleton
    fun provideModelProviderInterface(
        eventLoggerInterface: EventLoggerInterface,
        threadUtilInterface: ThreadUtilInterface,
        modelStorageInterface: ModelStorageInterface,
        preferencesInterface: Preferences,
        providerConfig: ProviderConfig,
    ): ModelProviderInterface = ModelProvider(
        eventLoggerInterface,
        threadUtilInterface,
        modelStorageInterface,
        preferencesInterface,
        providerConfig,
    )

    @Provides
    @Singleton
    fun provideScheduledSyncManager(@ApplicationContext appContext: Context): ScheduledSyncManager =
        DailySyncManager(appContext)

    @Provides
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Named(APP_CENTER_ID)
    fun provideAppCenterId(): String = BuildConfig.APP_CENTER_ID

    /**
     * Identifier for the App Center Id.
     */
    const val APP_CENTER_ID = "APP_CENTER_ID"
}
