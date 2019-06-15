package com.cramsan.petproject.storage

import com.cramsan.petproject.storage.implementation.ModelStorage
import com.cramsan.petproject.storage.implementation.ModelStorageInitializer

object ModelStorageAPI {

    private lateinit var initializer: ModelStorageInitializer

    val modelStorage: ModelStorageInterface by lazy { ModelStorage(initializer) }

    fun init(initializer: ModelStorageInitializer) {
        this.initializer = initializer
    }

}