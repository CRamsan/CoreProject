package com.cramsan.ps2link.appcore.census

import com.cramsan.ps2link.metric.HttpNamespace
import io.ktor.http.Url

data class UrlHolder(
    val urlIdentifier: HttpNamespace.Api,
    val completeUrl: Url
)
