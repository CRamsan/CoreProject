package com.cramsan.appcore

import android.content.Context
import com.cramsan.appcore.twitter.TwitterClientImpl
import com.cramsan.ps2link.appcore.census.DBGCensus
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.appcore.census.DBGServiceClientImpl
import com.cramsan.ps2link.appcore.census.buildHttpClient
import com.cramsan.ps2link.appcore.network.HttpClient
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appcore.repository.PS2LinkRepositoryImpl
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.appcore.sqldelight.SQLDelightDAO
import com.cramsan.ps2link.appcore.twitter.TwitterClient
import com.cramsan.ps2link.db.models.PS2LinkDB
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PS2AppCoreModule {

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
    ): SqlDriver {
        return AndroidSqliteDriver(PS2LinkDB.Schema, appContext, "ps2link.db")
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
    ): DBGServiceClient = DBGServiceClientImpl(dbgCensus, http)

    @Provides
    @Singleton
    fun provideKtorHttpClient(): io.ktor.client.HttpClient {
        return buildHttpClient()
    }

    @Provides
    @Singleton
    fun provideHttpClientImpl(http: io.ktor.client.HttpClient): HttpClient {
        return HttpClient(http)
    }

    @Provides
    @Singleton
    fun provideDbgCensus(): DBGCensus = DBGCensus()

    @Provides
    @Singleton
    fun provideTwitterClient(): TwitterClient = TwitterClientImpl()
}
