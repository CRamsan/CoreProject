package me.cesar.application.common.network

import kotlinx.serialization.Serializable
import me.cesar.application.common.model.SourceType

/**
 * Network model for persisting a new source.
 */
@Serializable
data class InsertionRequest(
    var title: String,
    var url: String,
    var sourceType: SourceType,
)
