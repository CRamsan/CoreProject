package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.sql.SQLException

actual class ModelStoragePlatformInitializer(val dbPath: String) {
    actual fun getSqlDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:$dbPath")
    }

    actual fun afterConnecting(driver: SqlDriver) {
        try {
            PetProjectDB.Schema.create(driver)
        } catch (e: SQLException) {
        }
    }
}
