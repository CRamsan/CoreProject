package com.cramsan.petproject.appcore.provider

import com.cramsan.petproject.appcore.provider.implementation.ModelProvider

/**
 * This class holds the configuration that [ModelProvider] needs to know
 * the API endpoints.
 */
data class ProviderConfig(
    /**
     * API Endpoint for fetching the plants.
     */
    val plantsEndpoint: String,
    /**
     * API Endpoint for fetching the main names.
     */
    val mainNameEndpoint: String,
    /**
     * API Endpoint for fetching the common names.
     */
    val commonNamesEndpoint: String,
    /**
     * API Endpoint for fetching the descriptions.
     */
    val descriptionsEndpoint: String,
    /**
     * API Endpoint for fetching the family.
     */
    val familyEndpoint: String,
    /**
     * API Endpoint for fetching the toxicity data.
     */
    val toxicityEndpoint: String,
)
