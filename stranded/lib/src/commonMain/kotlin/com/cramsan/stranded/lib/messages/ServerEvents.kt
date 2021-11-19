package com.cramsan.stranded.lib.messages

import com.cramsan.stranded.lib.game.logic.GameState
import com.cramsan.stranded.lib.game.models.state.Change
import com.cramsan.stranded.lib.repository.Lobby
import com.cramsan.stranded.lib.repository.Player
import kotlinx.serialization.Serializable

/**
 * Base class for all messages that are sent from the server to the client.
 */
@Serializable
sealed class ServerEvent

// TODO: Finish documenting these classes

@Serializable
object Ignore : ServerEvent()

@Serializable
data class Connected(val playerId: String) : ServerEvent()

@Serializable
object Disconnected : ServerEvent()

@Serializable
object GameStarted : ServerEvent()

@Serializable
data class LobbyCreatedFromRequest(val lobby: Lobby) : ServerEvent()

@Serializable
data class LobbyCreated(val lobby: Lobby) : ServerEvent()

@Serializable
data class LobbyDestroyed(val lobbyId: String) : ServerEvent()

@Serializable
data class JoinedLobby(val lobbyId: String, val player: Player) : ServerEvent()

@Serializable
data class LeftLobby(val lobbyId: String, val player: Player) : ServerEvent()

@Serializable
data class LobbyList(val lobbyList: List<Lobby>) : ServerEvent()

@Serializable
data class PlayerListFromRequest(val playerList: List<Player>) : ServerEvent()

@Serializable
data class PlayerUpdated(val player: Player) : ServerEvent()

@Serializable
data class GameChange(val change: Change) : ServerEvent()

@Serializable
data class GameStateMessage(val gameState: GameState) : ServerEvent()
