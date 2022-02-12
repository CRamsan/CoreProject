package com.cramsan.stranded.server.repository

import kotlinx.serialization.Serializable

/**
 * This class represents a lobby containing a list of players. The [id] is used to uniquely identify this lobby, while
 * the [name] is a friendly name that is used to display to the users. The [players] list contains a list of id that
 * map to the players currently in this lobby.
 */
@Serializable
data class Lobby(
    val id: String,
    val name: String,
    val players: List<String>,
    val maxPlayers: Int,
)
