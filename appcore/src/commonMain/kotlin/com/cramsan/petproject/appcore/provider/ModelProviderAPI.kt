package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.provider.implementation.ModelProvider
import com.cramsan.petproject.appcore.provider.implementation.ModelProviderInitializer

object ModelProviderAPI {

    private lateinit var initializer: ModelProviderInitializer

    val modelProvider: ModelProviderInterface by lazy { ModelProvider(initializer) }

    fun init(initializer: ModelProviderInitializer) {
        ModelProviderAPI.initializer = initializer
    }

}