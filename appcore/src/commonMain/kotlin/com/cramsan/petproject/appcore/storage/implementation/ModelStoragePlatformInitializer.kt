package com.cramsan.petproject.appcore.storage.implementation

import com.squareup.sqldelight.db.SqlDriver

expect class ModelStoragePlatformInitializer {
    fun getSqlDriver(): SqlDriver
}