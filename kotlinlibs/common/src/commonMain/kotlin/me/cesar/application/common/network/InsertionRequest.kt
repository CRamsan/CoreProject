package me.cesar.application.common.network

import me.cesar.application.common.model.SourceType

/**
 * Network model for persisting a new source.
 */
data class InsertionRequest(
    var title: String,
    var url: String,
    var sourceType: SourceType,
)
