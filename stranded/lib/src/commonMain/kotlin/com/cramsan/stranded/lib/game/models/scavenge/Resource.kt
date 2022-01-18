package com.cramsan.stranded.lib.game.models.scavenge

import kotlinx.serialization.Serializable

/**
 * [ScavengeResult] that has a [resourceType]. This card can be used for crafting.
 */
@Serializable
data class Resource(
    val resourceType: ResourceType,
) : ScavengeResult() {
    override val title = resourceType.name
}
