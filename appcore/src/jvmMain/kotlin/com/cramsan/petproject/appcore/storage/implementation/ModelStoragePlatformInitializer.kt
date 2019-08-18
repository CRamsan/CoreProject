package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class ModelStoragePlatformInitializer {
    actual fun getSqlDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:PetProject.sql")
    }

    actual fun afterConnecting(driver: SqlDriver) {
        try {
            PetProjectDB.Schema.create(driver)
        } catch (e: Exception) {

        }
    }
}
