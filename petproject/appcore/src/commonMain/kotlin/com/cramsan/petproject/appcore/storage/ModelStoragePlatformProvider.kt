package com.cramsan.petproject.appcore.storage

interface ModelStoragePlatformProvider {
    fun provide(): ModelStorageDAO
}
