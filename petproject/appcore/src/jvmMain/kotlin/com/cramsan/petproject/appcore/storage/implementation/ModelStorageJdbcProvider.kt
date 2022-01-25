package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.ModelStoragePlatformProvider
import com.cramsan.petproject.appcore.storage.implementation.sqldelight.SQLDelightDAO
import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.sql.SQLException

/**
 * JVM Implementation that uses the JdbcSqliteDriver to provide SQLite
 * storage on JVM platforms.
 */
class ModelStorageJdbcProvider(private val dbPath: String) :
    ModelStoragePlatformProvider {
    override fun provide(): ModelStorageDAO {
        val sqlDriver = JdbcSqliteDriver("jdbc:sqlite:$dbPath")
        val dao = SQLDelightDAO(sqlDriver)
        try {
            PetProjectDB.Schema.create(sqlDriver)
        } catch (e: SQLException) {
            throw e
        }
        return dao
    }
}
