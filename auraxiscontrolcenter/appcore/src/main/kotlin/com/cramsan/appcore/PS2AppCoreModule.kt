package com.cramsan.appcore

import com.cramsan.ps2link.db.models.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PS2AppCoreModule {

    @Provides
    @Singleton
    fun provideSqlDelightSchema(): SqlDriver.Schema = PS2LinkDB.Schema

    // TODO: This code is duplicated in a few places. We need to centralize these into a single function
    // in common code.
    @Provides
    @Singleton
    fun provideKotlinxSerializer() = Json {
        ignoreUnknownKeys = true
    }
}
