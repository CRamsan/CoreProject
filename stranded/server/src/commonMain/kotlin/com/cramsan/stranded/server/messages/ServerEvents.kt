package com.cramsan.stranded.server.messages

import com.cramsan.stranded.server.game.GameState
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.server.repository.Lobby
import com.cramsan.stranded.server.repository.Player
import kotlinx.serialization.Serializable

/**
 * Base class for all messages that are sent from the server to the client.
 */
@Serializable
sealed class ServerEvent

/**
 * The client is now connected to the server. The client can use [playerId] to identify itself.
 */
@Serializable
data class Connected(val playerId: String) : ServerEvent()

/**
 * The server has disconnected this client. No more events can be sent from this client.
 */
@Serializable
object Disconnected : ServerEvent()

/**
 * The server is notifying the client that the game has started. The client will now need to listen for [StateChange]
 * events to determine the state of the game.
 */
@Serializable
object GameStarted : ServerEvent()

/**
 * The server has created a lobby and it's notifying the caller with an instance of the [lobby].
 */
@Serializable
data class LobbyCreatedFromRequest(val lobby: Lobby) : ServerEvent()

/**
 * The [player] has joined the lobby with id [lobbyId]
 */
@Serializable
data class JoinedLobby(val lobbyId: String, val player: Player) : ServerEvent()

/**
 * The [player] has left the current lobby.
 */
@Serializable
data class LeftLobby(val player: Player) : ServerEvent()

/**
 * Event containing the [playerList] of all players in the current lobby.
 */
@Serializable
data class PlayerListFromRequest(val playerList: List<Player>) : ServerEvent()

/**
 * Message that notifies that a [player] has had a change in state.
 */
@Serializable
data class PlayerUpdated(val player: Player) : ServerEvent()

/**
 * The [change] represents a [StateChange] that is game-specific.
 */
@Serializable
data class GameChange(val change: StateChange) : ServerEvent()

/**
 * The [gameState] is a snapshot of the current state of the game.
 */
@Serializable
data class GameStateMessage(val gameState: GameState) : ServerEvent()
