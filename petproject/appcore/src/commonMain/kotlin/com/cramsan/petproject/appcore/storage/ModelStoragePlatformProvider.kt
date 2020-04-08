package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.storage.ModelStorageDAO

interface ModelStoragePlatformProvider {
    fun provide(): ModelStorageDAO
}
