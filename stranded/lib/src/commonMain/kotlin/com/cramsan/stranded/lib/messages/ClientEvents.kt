package com.cramsan.stranded.lib.messages

import com.cramsan.stranded.lib.game.intent.PlayerIntent
import kotlinx.serialization.Serializable

/**
 * Base class for all messages that are sent from the client to the server.
 */
@Serializable
sealed class ClientEvent

// TODO: Finish documenting these classes.

@Serializable
object Ping : ClientEvent()

@Serializable
object ListLobbies : ClientEvent()

@Serializable
object ListPlayers : ClientEvent()

@Serializable
data class CreateLobby(val lobbyName: String) : ClientEvent()

@Serializable
data class JoinLobby(val lobbyId: String) : ClientEvent()

@Serializable
object LeaveLobby : ClientEvent()

@Serializable
data class DeleteLobby(val lobbyId: String) : ClientEvent()

@Serializable
object StartGame : ClientEvent()

@Serializable
data class SetPlayerName(val playerName: String) : ClientEvent()

@Serializable
object ReadyToStartGame : ClientEvent()

@Serializable
data class SetReadyToStart(val readyToStart: Boolean) : ClientEvent()

@Serializable
data class GamePlayerIntent(val playerIntent: PlayerIntent) : ClientEvent()
