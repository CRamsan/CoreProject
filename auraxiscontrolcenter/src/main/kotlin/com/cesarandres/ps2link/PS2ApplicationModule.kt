package com.cesarandres.ps2link

import android.content.Context
import com.cramsan.framework.assert.AssertUtil
import com.cramsan.framework.assert.AssertUtilInterface
import com.cramsan.framework.assert.implementation.AssertUtilImpl
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.DispatcherProviderImpl
import com.cramsan.framework.crashehandler.CrashHandler
import com.cramsan.framework.crashehandler.CrashHandlerDelegate
import com.cramsan.framework.crashehandler.implementation.AppCenterCrashHandler
import com.cramsan.framework.crashehandler.implementation.CrashHandlerImpl
import com.cramsan.framework.halt.HaltUtil
import com.cramsan.framework.halt.HaltUtilDelegate
import com.cramsan.framework.halt.implementation.HaltUtilAndroid
import com.cramsan.framework.halt.implementation.HaltUtilImpl
import com.cramsan.framework.logging.EventLogger
import com.cramsan.framework.logging.EventLoggerDelegate
import com.cramsan.framework.logging.EventLoggerErrorCallbackInterface
import com.cramsan.framework.logging.EventLoggerInterface
import com.cramsan.framework.logging.Severity
import com.cramsan.framework.logging.implementation.EventLoggerImpl
import com.cramsan.framework.logging.implementation.LoggerAndroid
import com.cramsan.framework.metrics.Metrics
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.AppCenterMetrics
import com.cramsan.framework.metrics.implementation.MetricsErrorCallback
import com.cramsan.framework.metrics.implementation.MetricsImpl
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.preferences.implementation.PreferencesImpl
import com.cramsan.framework.thread.ThreadUtil
import com.cramsan.framework.thread.ThreadUtilDelegate
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.framework.thread.implementation.ThreadUtilAndroid
import com.cramsan.framework.thread.implementation.ThreadUtilImpl
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.preferences.PS2SettingsImpl
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.appcore.repository.RedditRepositoryImpl
import com.cramsan.ps2link.appcore.repository.TwitterRepository
import com.cramsan.ps2link.appcore.repository.TwitterRepositoryImpl
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PS2ApplicationModule {

    @Provides
    @Singleton
    fun provideAssertUtil(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): AssertUtilInterface {
        // TODO: This is not working. I assume this is a Hilt bug
        val impl = AssertUtilImpl(
            BuildConfig.DEBUG,
            eventLoggerInterface,
            haltUtilInterface
        )
        return AssertUtil.instance(impl)
    }

    @Provides
    @Singleton
    fun provideThreadUtilDelegate(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): ThreadUtilDelegate {
        return ThreadUtilAndroid(
            AssertUtilImpl(
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
    fun provideCrashHandlerInterface(crashHandlerDelegate: CrashHandlerDelegate): CrashHandler =
        CrashHandlerImpl(crashHandlerDelegate)

    @Provides
    @Singleton
    fun provideMetricsDelegate(): MetricsDelegate = AppCenterMetrics()

    @Provides
    @Singleton
    fun provideMetricsInterface(metricsDelegate: MetricsDelegate): MetricsInterface {
        val instance = MetricsImpl(metricsDelegate)
        return Metrics.instance(instance)
    }

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
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val severity: Severity = when (BuildConfig.DEBUG) {
            true -> Severity.DEBUG
            false -> Severity.INFO
        }
        val instance =
            EventLoggerImpl(severity, eventLoggerErrorCallbackInterface, eventLoggerDelegate)
        return EventLogger.instance(instance)
    }

    @Provides
    @Singleton
    fun provideHaltUtilDelegate(): HaltUtilDelegate = HaltUtilAndroid()

    @Provides
    @Singleton
    fun provideHaltUtilInterface(haltUtilDelegate: HaltUtilDelegate): HaltUtil =
        HaltUtilImpl(haltUtilDelegate)

    @Provides
    @Singleton
    fun provideThreadUtilInterface(threadUtilDelegate: ThreadUtilDelegate): ThreadUtilInterface {
        val instance = ThreadUtilImpl(threadUtilDelegate)
        return ThreadUtil.instance(instance)
    }

    @Provides
    @Singleton
    fun providePreferencesDelegate(@ApplicationContext appContext: Context): PreferencesDelegate =
        PreferencesAndroid(
            appContext
        )

    @Provides
    @Singleton
    fun providePreferencesInterface(preferencesDelegate: PreferencesDelegate): Preferences =
        PreferencesImpl(preferencesDelegate)

    @Provides
    @Singleton
    fun provideDispatcher(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Singleton
    fun providePS2Settings(
        preferencesInterface: Preferences,
    ): PS2Settings = PS2SettingsImpl(preferencesInterface)

    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun provideRedditRepository(
        http: HttpClient,
    ): RedditRepository = RedditRepositoryImpl(http)

    @Provides
    @Singleton
    fun providePrettyTime(): PrettyTime = PrettyTime()

    @Provides
    @Singleton
    fun provideTwitterRepository(
        twitterClient: TwitterClient,
        preferences: Preferences,
        dispatcherProvider: DispatcherProvider,
    ): TwitterRepository = TwitterRepositoryImpl(twitterClient, preferences, dispatcherProvider)
}
