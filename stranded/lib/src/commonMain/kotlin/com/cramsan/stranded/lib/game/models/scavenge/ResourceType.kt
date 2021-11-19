package com.cramsan.stranded.lib.game.models.scavenge

import kotlinx.serialization.Serializable

/**
 * Define the type of resource. These resources will be used for crafting.
 */
@Serializable
enum class ResourceType {
    STICK,
    FIBER,
    ROCK,
    BONE,
}
