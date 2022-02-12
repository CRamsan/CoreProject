package com.cramsan.stranded.server.repository

import kotlinx.serialization.Serializable

/**
 * Represents a player with unique Id [id]. The [name] is a string to display to other users. The [readyToStart] boolean
 * is used before the game starts to determine if all players in a lobby are ready to start the game.
 */
@Serializable
data class Player(
    val id: String,
    val name: String,
    val readyToStart: Boolean
) {
    companion object {
        val disconnectedPlayer = Player("", "", false)
    }
}
