package com.cramsan.stranded.lib.game.models.crafting

import kotlinx.serialization.Serializable

/**
 * A [Shelter] can provide some side effects to some cards. The [playerList] will contain the
 * [com.cramsan.stranded.lib.game.models.GamePlayer.id] of the players using this instance of [Shelter].
 * Only up to [MAX_OCCUPANCY] can be part of [playerList].
 */
@Serializable
data class Shelter(
    val playerList: MutableList<String>,
) : Craftable() {
    override val title = "Shelter"

    companion object {
        const val MAX_OCCUPANCY = 3
    }
}
