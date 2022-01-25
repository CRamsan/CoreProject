package com.cramsan.petproject.appcore.storage

/**
 * Implement this interface to provide your own implementation
 * of a [ModelStorageDAO].
 */
interface ModelStoragePlatformProvider {
    /**
     * Initialize a new instance of [ModelStorageDAO].
     */
    fun provide(): ModelStorageDAO
}
