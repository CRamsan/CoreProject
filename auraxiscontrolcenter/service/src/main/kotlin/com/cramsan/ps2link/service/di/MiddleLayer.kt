package com.cramsan.ps2link.service.di

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.network.ws.Environment
import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.service.controller.census.DBGCensus
import com.cramsan.ps2link.service.controller.census.DBGServiceClient
import com.cramsan.ps2link.service.repository.mongo.models.Character
import com.cramsan.ps2link.service.repository.mongo.models.Item
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

/**
 * Initialize classes to be used by the application mid-layer, which comes from the PS2Link:appcore package. These
 * classes hold business logic to interface with the PS2 API and are not directly tied to the current application.
 */
val DomainModule = module {

    single {
        val client = KMongo.createClient().coroutine
        client.getDatabase("PS2LinkDatabase")
    }

    single(named(COLLECTION_CHARACTER_NAME)) {
        val database: CoroutineDatabase = get()
        database.getCollection<Character>()
    }

    single(named(COLLECTION_ITEM_NAME)) {
        val database: CoroutineDatabase = get()
        database.getCollection<Item>()
    }


    single<CoroutineDispatcher> {
        val dispatcherProvider: DispatcherProvider = get()
        dispatcherProvider.ioDispatcher()
    }

    single<Json> {
        Json {
            prettyPrint = false
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                classDiscriminator = "_type"
            }
        }
    }

    single<String>(named(SERVICE_ID_NAME)) {
        "ps2link"
    }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
            install(WebSockets)
        }
    }

    single<Clock> {
        Clock.System
    }

    single<DBGCensus> {
        DBGCensus(
            get(named(SERVICE_ID_NAME)),
        )
    }

    single<DBGServiceClient> {
        DBGServiceClient(
            get(),
            get(),
        )
    }

    single<CoroutineScope> {
        val dispatcherProvider: DispatcherProvider = get()
        CoroutineScope(SupervisorJob() + dispatcherProvider.ioDispatcher())
    }

    single<StreamingClient> {
        StreamingClient(
            get(),
            get(),
            get(named(SERVICE_ID_NAME)),
            Environment.PS2,
            get(),
        )
    }

    factory<StreamingClient>(named(WS_CLIENT)) {
        StreamingClient(
            get(),
            get(),
            get(named(SERVICE_ID_NAME)),
            Environment.PS2,
            get(),
        )
    }
}

const val SERVICE_ID_NAME = "serviceId"
const val COLLECTION_CHARACTER_NAME = "collection_character"
const val COLLECTION_ITEM_NAME = "collection_item"
const val WS_CLIENT = "ws_client_factory"
