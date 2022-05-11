package com.cesarandres.ps2link

import android.content.Context
import android.content.res.Resources
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.APP_CENTER_ID
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.APP_SCOPE
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.AWS_ACCESS_KEY
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.AWS_SECRET_KEY
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.CENSUS_SERVICE_ID
import com.cesarandres.ps2link.PS2ApplicationModuleConstants.REMOTE_CONFIG_ENDPOINT
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
import com.cramsan.framework.metrics.MetricsDelegate
import com.cramsan.framework.metrics.MetricsInterface
import com.cramsan.framework.metrics.implementation.CloudwatchMetrics
import com.cramsan.framework.metrics.implementation.MetricsErrorCallback
import com.cramsan.framework.metrics.implementation.MetricsImpl
import com.cramsan.framework.preferences.Preferences
import com.cramsan.framework.preferences.PreferencesDelegate
import com.cramsan.framework.preferences.implementation.PreferencesAndroid
import com.cramsan.framework.preferences.implementation.PreferencesImpl
import com.cramsan.framework.remoteconfig.RemoteConfig
import com.cramsan.framework.remoteconfig.implementation.RemoteConfigImpl
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
import com.cramsan.ps2link.remoteconfig.RemoteConfigData
import com.cramsan.ps2link.remoteconfig.defaultConfigPayload
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.Clock
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.ocpsoft.prettytime.PrettyTime
import javax.inject.Named
import javax.inject.Singleton

@Module
@Suppress("UndocumentedPublicFunction")
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
    @Named(PS2ApplicationModuleConstants.ERROR_CALLBACK_METRICS_IMPL)
    fun provideMetricsBasedEventLoggerErrorCallbackDelegate(
        metrics: MetricsInterface,
        eventLogger: EventLoggerInterface,
        ioDispatcherProvider: DispatcherProvider,
        @Named(APP_SCOPE)
        scope: CoroutineScope,
    ): EventLoggerErrorCallbackDelegate = MetricsErrorCallback(
        metrics,
        eventLogger,
        ioDispatcherProvider,
        scope,
    )

    @Provides
    @Singleton
    fun provideEventLoggerErrorCallbackDelegate(
        @Named(PS2ApplicationModuleConstants.ERROR_CALLBACK_METRICS_IMPL)
        errorCallback: EventLoggerErrorCallbackDelegate,
    ): EventLoggerErrorCallbackDelegate =
        AppCenterErrorCallback(errorCallback)

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
        eventLoggerDelegate: EventLoggerDelegate,
    ): EventLoggerInterface {
        val severity: Severity = when (BuildConfig.DEBUG) {
            true -> Severity.DEBUG
            false -> Severity.INFO
        }
        val instance =
            EventLoggerImpl(severity, null, eventLoggerDelegate)
        EventLogger.setInstance(instance)
        return EventLogger.singleton
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
        ThreadUtil.setInstance(instance)
        return ThreadUtil.singleton
    }

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
    fun provideDispatcher(): DispatcherProvider = DispatcherProviderImpl()

    @Singleton
    @Provides
    @Named(APP_SCOPE)
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

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
    fun provideHttpClient(
        httpClient: io.ktor.client.HttpClient,
        json: Json,
        metricsInterface: MetricsInterface,
    ): HttpClient {
        return HttpClient(httpClient, json, metricsInterface)
    }

    @Provides
    @Named(AWS_ACCESS_KEY)
    fun provideAwsAccessKey(resources: Resources): String = resources.getString(R.string.aws_access_key)

    @Provides
    @Named(AWS_SECRET_KEY)
    fun provideAwsSecretKey(resources: Resources): String = resources.getString(R.string.aws_secret_key)

    @Provides
    @Singleton
    fun provideMetricsDelegate(
        @Named(AWS_ACCESS_KEY) accessKey: String,
        @Named(AWS_SECRET_KEY) secretKey: String,
        dispatcherProvider: DispatcherProvider,
        eventLogger: EventLoggerInterface,
        @Named(APP_SCOPE) scope: CoroutineScope,
    ): MetricsDelegate {
        return CloudwatchMetrics.createInstance(
            accessKey,
            secretKey,
            dispatcherProvider,
            eventLogger,
            scope,
        ).apply {
            initialize()
        }
    }

    @Provides
    @Singleton
    fun provideMetricsInterface(delegate: MetricsDelegate, eventLogger: EventLoggerInterface): MetricsInterface {
        return MetricsImpl(delegate, eventLogger).apply {
            initialize()
        }
    }

    @Provides
    @Singleton
    fun provideDbgCensus(@Named(CENSUS_SERVICE_ID) serviceId: String): DBGCensus = DBGCensus(serviceId)

    @Provides
    @Named(CENSUS_SERVICE_ID)
    fun provideServiceId(): String {
        return BuildConfig.CENSUS_SERVICE_ID
    }

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
        dispatcherProvider: DispatcherProvider,
        remoteConfig: RemoteConfig<RemoteConfigData>,
    ): TwitterRepository = TwitterRepositoryImpl(twitterClient, remoteConfig, dispatcherProvider)

    @Provides
    fun provideResources(@ApplicationContext appContext: Context): Resources = appContext.resources

    @Provides
    @Singleton
    fun provideObjectDataSource(@ApplicationContext appContext: Context) =
        ObjectDataSource(appContext)

    @Provides
    @Named(REMOTE_CONFIG_ENDPOINT)
    fun provideRemoteConfigEndpoint(@ApplicationContext context: Context) = context.resources.getString(
        R.string.remote_config_url,
    )

    @OptIn(InternalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRemoteConfig(
        @Named(REMOTE_CONFIG_ENDPOINT) remoteConfigEndpoint: String,
        http: io.ktor.client.HttpClient,
        json: Json,
        threadUtil: ThreadUtilInterface,
        eventLogger: EventLoggerInterface,
        dispatcherProvider: DispatcherProvider,
        @Named(APP_SCOPE) scope: CoroutineScope,
    ): RemoteConfig<RemoteConfigData> = RemoteConfigImpl(
        remoteConfigEndpoint,
        http,
        json,
        defaultConfigPayload,
        RemoteConfigData::class.serializer(),
        threadUtil,
        eventLogger,
        dispatcherProvider,
        scope,
    )
}
