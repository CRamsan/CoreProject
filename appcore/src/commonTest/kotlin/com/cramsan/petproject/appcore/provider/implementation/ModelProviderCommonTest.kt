package com.cramsan.petproject.appcore.provider.implementation

import com.cramsan.petproject.appcore.provider.ModelProviderInterface

internal class ModelProviderCommonTest {

    private lateinit var modelProvider: ModelProviderInterface
    private lateinit var modelProviderImpl: ModelProvider

    fun setUp(platformInitializer: ModelProviderPlatformInitializer) {
        val initializer = ModelProviderInitializer(platformInitializer)
        modelProviderImpl = ModelProvider(initializer)
        modelProvider = modelProviderImpl
    }

    fun tearDown() {

    }
}
