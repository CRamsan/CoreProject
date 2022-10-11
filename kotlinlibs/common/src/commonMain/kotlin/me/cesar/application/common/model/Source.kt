package me.cesar.application.common.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Domain model for representing the information about a single internet source.
 *
 * @author cramsan
 */
@Serializable
data class Source(
    val id: String,
    val title: String,
    val url: String,
    val lastUpdated: Instant,
    val sourceType: SourceType,
)
