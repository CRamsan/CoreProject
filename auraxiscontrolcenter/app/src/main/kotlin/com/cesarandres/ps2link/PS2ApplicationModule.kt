package com.cesarandres.ps2link

import android.content.Context
import android.content.res.Resources
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.APP_CENTER_ID
import com.cesarandres.ps2link.deprecated.module.ObjectDataSource
import com.cramsan.appcore.twitter.TwitterClientImpl
import com.cramsan.appcore.twitter.TwitterModuleConstants.ACCESS_TOKEN
import com.cramsan.appcore.twitter.TwitterModuleConstants.ACCESS_TOKEN_SECRET
import com.cramsan.appcore.twitter.TwitterModuleConstants.CONSUMER_KEY
import com.cramsan.appcore.twitter.TwitterModuleConstants.CONSUMER_SECRET
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
import com.cramsan.framework.metrics.Metrics
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.AppCenterMetrics
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
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.census.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.census.buildHttpClient
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.preferences.PS2SettingsImpl
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.PS2LinkRepositoryImpl
import com.cramsan.ps2link.appcore.repository.RedditRepository
import com.cramsan.ps2link.appcore.repository.RedditRepositoryImpl
import com.cramsan.ps2link.appcore.repository.TwitterRepository
import com.cramsan.ps2link.appcore.repository.TwitterRepositoryImpl
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.sqldelight.SQLDelightDAO
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PS2ApplicationModule {

    @Provides
    @Singleton
    fun provideAssertUtil(
        eventLoggerInterface: EventLoggerInterface,
        haltUtilInterface: HaltUtil,
    ): AssertUtilInterface {
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
    fun provideMetricsDelegate(): MetricsDelegate = AppCenterMetrics()

    @Provides
    @Singleton
    fun provideMetricsInterface(
        metricsDelegate: MetricsDelegate,
        eventLoggerInterface: EventLoggerInterface,
    ): MetricsInterface {
        val instance = MetricsImpl(metricsDelegate, eventLoggerInterface)
        return Metrics.instance(instance)
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
        eventLoggerErrorCallback: EventLoggerErrorCallback,
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val severity: Severity = when (BuildConfig.DEBUG) {
            true -> Severity.DEBUG
            false -> Severity.INFO
        }
        val instance =
            EventLoggerImpl(severity, eventLoggerErrorCallback, eventLoggerDelegate)
        return EventLogger.instance(instance)
    }

    @Provides
    @Singleton
    fun provideHaltUtilDelegate(@ApplicationContext appContext: Context): HaltUtilDelegate =
        HaltUtilAndroid(appContext)

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
    fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    fun providePrettyTime(): PrettyTime = PrettyTime()

    @Provides
    @Singleton
    fun provideDbgDao(
        sqlDriver: SqlDriver,
        clock: Clock,
    ): DbgDAO = SQLDelightDAO(sqlDriver, clock)

    @Provides
    @Singleton
    fun provideSqlDelightDriver(
        @ApplicationContext appContext: Context,
        schema: SqlDriver.Schema,
    ): SqlDriver {
        return AndroidSqliteDriver(schema, appContext, "ps2link2.db")
    }

    @Provides
    @Singleton
    fun providePS2LinkRepository(
        dbgServiceClient: DBGServiceClient,
        dbgDAO: DbgDAO,
        clock: Clock,
    ): PS2LinkRepository = PS2LinkRepositoryImpl(dbgServiceClient, dbgDAO, clock)

    @Provides
    @Singleton
    fun provideDbgServiceClient(
        dbgCensus: DBGCensus,
        http: HttpClient,
        clock: Clock,
    ): DBGServiceClient = DBGServiceClientImpl(dbgCensus, http, clock)

    @Provides
    @Singleton
    fun provideKtorHttpClient(json: Json): io.ktor.client.HttpClient {
        return buildHttpClient(json)
    }

    @Provides
    @Singleton
    fun provideHttpClient(httpClient: io.ktor.client.HttpClient, json: Json): HttpClient {
        return HttpClient(httpClient, json)
    }

    @Provides
    @Singleton
    fun provideDbgCensus(): DBGCensus = DBGCensus()

    @Provides
    @Named(CONSUMER_SECRET)
    fun provideTwitterConsumerSecret(): String = BuildConfig.CONSUMER_SECRET

    @Provides
    @Named(CONSUMER_KEY)
    fun provideTwitterConsumerKey(): String = BuildConfig.CONSUMER_KEY

    @Provides
    @Named(ACCESS_TOKEN)
    fun provideTwitterAccessToken(): String = BuildConfig.ACCESS_TOKEN

    @Provides
    @Named(ACCESS_TOKEN_SECRET)
    fun provideTwitterAccessTokenSecret(): String = BuildConfig.ACCESS_TOKEN_SECRET

    @Provides
    @Named(APP_CENTER_ID)
    fun provideAppCenterId(): String = BuildConfig.APP_CENTER_ID

    @Provides
    @Singleton
    fun provideTwitterClient(
        @Named(CONSUMER_SECRET) consumerSecret: String,
        @Named(CONSUMER_KEY) consumerKey: String,
        @Named(ACCESS_TOKEN) accessToken: String,
        @Named(ACCESS_TOKEN_SECRET) accessTokenSecret: String,
    ): TwitterClient = TwitterClientImpl(
        consumerSecret,
        consumerKey,
        accessToken,
        accessTokenSecret,
    )

    @Provides
    @Singleton
    fun provideRedditRepository(
        http: HttpClient,
    ): RedditRepository = RedditRepositoryImpl(http)

    @Provides
    @Singleton
    fun providePS2Settings(
        preferencesInterface: Preferences,
    ): PS2Settings = PS2SettingsImpl(preferencesInterface)

    @Provides
    @Singleton
    fun provideTwitterRepository(
        twitterClient: TwitterClient,
        preferences: Preferences,
        dispatcherProvider: DispatcherProvider,
    ): TwitterRepository = TwitterRepositoryImpl(twitterClient, preferences, dispatcherProvider)

    @Provides
    fun provideResources(@ApplicationContext appContext: Context): Resources = appContext.resources

    @Provides
    @Singleton
    fun provideObjectDataSource(@ApplicationContext appContext: Context) =
        ObjectDataSource(appContext)
}