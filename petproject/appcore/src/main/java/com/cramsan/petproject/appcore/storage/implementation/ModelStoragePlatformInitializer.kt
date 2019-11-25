package com.cramsan.petproject.appcore.storage.implementation

import android.content.Context
import com.cramsan.petproject.appcore.storage.ModelStorageDAO
import com.cramsan.petproject.appcore.storage.implementation.sqldelight.SQLDelightDAO
import com.cramsan.petproject.db.PetProjectDB
import com.squareup.sqldelight.android.AndroidSqliteDriver

actual class ModelStoragePlatformInitializer(val context: Context) {
    actual fun getModelStorageDAO(): ModelStorageDAO {
        val sqlDriver = AndroidSqliteDriver(PetProjectDB.Schema, context, "petproject.db")
        return SQLDelightDAO(sqlDriver)
    }
}
