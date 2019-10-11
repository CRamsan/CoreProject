package com.cramsan.petproject.appcore.storage.implementation

import com.cramsan.petproject.appcore.storage.ModelStorageDAO

expect class ModelStoragePlatformInitializer {
    fun getModelStorageDAO(): ModelStorageDAO
}
