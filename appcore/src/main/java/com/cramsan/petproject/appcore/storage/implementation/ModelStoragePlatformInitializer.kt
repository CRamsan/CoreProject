package com.cramsan.petproject.appcore.storage.implementation

import android.content.Context
import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class ModelStoragePlatformInitializer(val context: Context) {
    actual fun getSqlDriver(): SqlDriver {
        return AndroidSqliteDriver(PetProjectDB.Schema, context, "petproject.db")
    }

    actual fun afterConnecting(driver: SqlDriver) {
    }
}
