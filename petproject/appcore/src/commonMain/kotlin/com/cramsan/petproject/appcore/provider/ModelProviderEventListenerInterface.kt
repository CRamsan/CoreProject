package com.cramsan.petproject.appcore.provider

/**
 * Callbacks for events triggered from [ModelProviderInterface].
 */
interface ModelProviderEventListenerInterface {

    /**
     * The catalog has changed states. The new value is [isReady].
     */
    fun onCatalogUpdate(isReady: Boolean)
}
