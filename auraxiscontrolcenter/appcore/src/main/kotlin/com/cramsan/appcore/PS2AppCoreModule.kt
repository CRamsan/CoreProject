package com.cramsan.appcore

import com.cramsan.ps2link.db.models.PS2LinkDB
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PS2AppCoreModule {

    @Provides
    @Singleton
    fun provideSqlDelightSchema(): SqlDriver.Schema = PS2LinkDB.Schema
}
