package com.cramsan.petproject.appcore.provider

data class ProviderConfig(
    val plantsEndpoint: String,
    val mainNameEndpoint: String,
    val commonNamesEndpoint: String,
    val descriptionsEndpoint: String,
    val familyEndpoint: String,
    val toxicityEndpoint: String
)
