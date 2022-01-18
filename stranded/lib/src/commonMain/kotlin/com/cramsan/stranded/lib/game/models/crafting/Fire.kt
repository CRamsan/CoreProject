package com.cramsan.stranded.lib.game.models.crafting

import kotlinx.serialization.Serializable

/**
 * A fire that lasts for a single night. Having a [Fire] can enable some side effects from other cards.
 */
@Serializable
class Fire : Craftable() {
    override val title = "Fire"
}
