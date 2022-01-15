package com.cramsan.stranded.lib.game.models.scavenge

import kotlinx.serialization.Serializable

/**
 * [ScavengeResult] that has a [resourceType]. This card can be used for crafting.
 */
@Serializable
class Resource(val resourceType: ResourceType) : ScavengeResult(resourceType.name)
