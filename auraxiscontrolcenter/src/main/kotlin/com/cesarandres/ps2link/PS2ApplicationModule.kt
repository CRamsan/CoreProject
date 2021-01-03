package com.cesarandres.ps2link

import android.content.Context
import com.cramsan.framework.assert.implementation.AssertUtil
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.core.DispatcherProviderImpl
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
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.dbg.DBGCensus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PS2ApplicationModule {

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
    fun provideDispatcher(): DispatcherProvider = DispatcherProviderImpl()

    @Provides
    @Singleton
    fun provideDbgCensus(eventLogger: EventLoggerInterface): DBGCensus = DBGCensus(eventLogger)

    @Provides
    @Singleton
    fun provideDbgServiceClient(
        eventLogger: EventLoggerInterface,
        metricsClient: MetricsInterface,
        dbgCensus: DBGCensus,
    ): DBGServiceClient = DBGServiceClientImpl(eventLogger, metricsClient, dbgCensus)
}
