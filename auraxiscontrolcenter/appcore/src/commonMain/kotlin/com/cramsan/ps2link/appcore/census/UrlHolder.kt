package com.cramsan.ps2link.appcore.census

import io.ktor.http.Url

data class UrlHolder(
    val urlIdentifier: String,
    val completeUrl: Url
)
