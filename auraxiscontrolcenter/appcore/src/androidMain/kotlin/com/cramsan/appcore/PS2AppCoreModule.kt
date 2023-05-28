package com.cramsan.appcore

import com.cramsan.ps2link.db.models.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

/**
 * Singleton components for the core components of the PS2App.
 */
@Module
@InstallIn(SingletonComponent::class)
object PS2AppCoreModule {

    /**
     * Provide the [SqlDriver.Schema]
     */
    @Provides
    @Singleton
    fun provideSqlDelightSchema(): SqlDriver.Schema = PS2LinkDB.Schema

    /**
     * Provide the implementation of the Kotlinx Json serializer.
     */
    // TODO: This code is duplicated in a few places. We need to centralize these into a single function
    // in common code.
    @Provides
    @Singleton
    fun provideKotlinxSerializer() = Json {
        ignoreUnknownKeys = true
    }
}
