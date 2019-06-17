package com.cramsan.petproject.appcore.storage

import com.cramsan.petproject.appcore.storage.implementation.ModelStorage
import com.cramsan.petproject.appcore.storage.implementation.ModelStorageInitializer

object ModelStorageAPI {

    private lateinit var initializer: ModelStorageInitializer

    val modelStorage: ModelStorageInterface by lazy { ModelStorage(initializer) }

    fun init(initializer: ModelStorageInitializer) {
        ModelStorageAPI.initializer = initializer
    }

}