package com.cramsan.stranded.server.messages

import com.cramsan.stranded.server.game.PlayerIntent
import kotlinx.serialization.Serializable

/**
 * Base class for all messages that are sent from the client to the server.
 */
@Serializable
sealed class ClientEvent

/**
 * Ping. Used to verify connectivity.
 */
@Serializable
object Ping : ClientEvent()

/**
 * Request the list of players in the current lobby.
 */
@Serializable
object ListPlayers : ClientEvent()

/**
 * Request to create a lobby with the provided [lobbyName].
 */
@Serializable
data class CreateLobby(val lobbyName: String) : ClientEvent()

/**
 * Request to join a lobby identified by the provided [lobbyId]
 */
@Serializable
data class JoinLobby(val lobbyId: String) : ClientEvent()

/**
 * Request to leave the current lobby.
 */
@Serializable
object LeaveLobby : ClientEvent()

/**
 * Request to delete a lobby identified by [lobbyId].
 */
@Serializable
object DeleteLobby : ClientEvent()

/**
 * Request to start the game in the current lobby.
 */
@Serializable
object StartGame : ClientEvent()

/**
 * Request to set the name of the current player to [playerName].
 */
@Serializable
data class SetPlayerName(val playerName: String) : ClientEvent()

/**
 * Request to update the isReady status of the player in this lobby to [isReady].
 */
@Serializable
data class SetReadyToStartGame(val isReady: Boolean) : ClientEvent()

/**
 * Send a [playerIntent] to the server so it can be handled.
 */
@Serializable
data class GamePlayerIntent(val playerIntent: PlayerIntent) : ClientEvent()
