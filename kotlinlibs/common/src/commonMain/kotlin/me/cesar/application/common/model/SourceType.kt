package me.cesar.application.common.model

import kotlinx.serialization.Serializable

/**
 * Enum to determine the protocol used to fetch a [Source].
 */
@Serializable
enum class SourceType {
    RSS,
    UNKNOWN,
}
