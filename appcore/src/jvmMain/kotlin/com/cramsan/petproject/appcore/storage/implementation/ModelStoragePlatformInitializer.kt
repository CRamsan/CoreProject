package com.cramsan.petproject.appcore.storage.implementation

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class ModelStoragePlatformInitializer {
    actual fun getSqlDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:test")
    }
}
