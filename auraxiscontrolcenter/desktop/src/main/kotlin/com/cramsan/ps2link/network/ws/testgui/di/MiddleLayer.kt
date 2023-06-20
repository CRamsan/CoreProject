package com.cramsan.ps2link.network.ws.testgui.di

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.preferences.PS2SettingsImpl
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.sqldelight.SQLDelightDAO
import com.cramsan.ps2link.db.models.PS2LinkDB
import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.testgui.ApplicationModule
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Initialize classes to be used by the application mid-layer, which comes from the PS2Link:appcore package. These
 * classes hold business logic to interface with the PS2 API and are not directly tied to the current application.
 */
val DomainModule = module {

    single<CoroutineDispatcher> {
        val dispatcherProvider: DispatcherProvider = get()
        dispatcherProvider.ioDispatcher()
    }

    single<Json> {
        ApplicationModule.provideJson()
    }

    single<String>(named(SERVICE_ID_NAME)) {
        ApplicationModule.provideServiceId()
    }

    single<HttpClient> {
        ApplicationModule.provideKtorHttpClient(get())
    }

    single<com.cramsan.ps2link.appcore.network.HttpClient> {
        ApplicationModule.provideHttpClient(
            get(),
            get(),
        )
    }

    single<Clock> {
        ApplicationModule.provideClock()
    }

    single<DBGCensus> {
        ApplicationModule.provideDbgCensus(get(named(SERVICE_ID_NAME)))
    }

    single<DBGServiceClient> {
        ApplicationModule.provideDbgServiceClient(
            get(),
            get(),
            get(),
        )
    }

    single<CoroutineScope> {
        ApplicationModule.providesApplicationCoroutineScope()
    }

    single<DbgDAO> {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        PS2LinkDB.Schema.create(driver)
        SQLDelightDAO(driver, get())
    }

    single<StreamingClient> {
        ApplicationModule.provideStreamingClient(
            get(),
            get(),
            get(named(SERVICE_ID_NAME)),
            get(),
        )
    }

    single<PS2Settings> {
        PS2SettingsImpl(get())
    }
}

private const val SERVICE_ID_NAME = "serviceId"
