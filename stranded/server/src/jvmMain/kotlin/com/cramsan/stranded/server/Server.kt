package com.cramsan.stranded.server

import com.cramsan.stranded.server.game.PlayerIntent
import com.cramsan.stranded.server.game.StateChange
import com.cramsan.stranded.server.messages.ClientEvent
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.CreateLobby
import com.cramsan.stranded.server.messages.DeleteLobby
import com.cramsan.stranded.server.messages.Disconnected
import com.cramsan.stranded.server.messages.GameChange
import com.cramsan.stranded.server.messages.GamePlayerIntent
import com.cramsan.stranded.server.messages.GameStarted
import com.cramsan.stranded.server.messages.GameStateMessage
import com.cramsan.stranded.server.messages.JoinLobby
import com.cramsan.stranded.server.messages.JoinedLobby
import com.cramsan.stranded.server.messages.LeaveLobby
import com.cramsan.stranded.server.messages.LeftLobby
import com.cramsan.stranded.server.messages.ListPlayers
import com.cramsan.stranded.server.messages.LobbyCreatedFromRequest
import com.cramsan.stranded.server.messages.Ping
import com.cramsan.stranded.server.messages.PlayerListFromRequest
import com.cramsan.stranded.server.messages.PlayerUpdated
import com.cramsan.stranded.server.messages.ServerEvent
import com.cramsan.stranded.server.messages.SetPlayerName
import com.cramsan.stranded.server.messages.SetReadyToStartGame
import com.cramsan.stranded.server.messages.StartGame
import com.cramsan.stranded.server.messages.createSerializedMessage
import com.cramsan.stranded.server.messages.parseClientEvent
import com.cramsan.stranded.server.repository.ConnectionRepository
import com.cramsan.stranded.server.repository.GameRepository
import com.cramsan.stranded.server.repository.LobbyRepository
import com.cramsan.stranded.server.repository.Player
import com.cramsan.stranded.server.repository.PlayerRepository
import com.cramsan.stranded.server.utils.generateUUID
import io.ktor.application.install
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class Server(
    private val lobbyRepository: LobbyRepository,
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val connectionRepository: ConnectionRepository,
    private val json: Json,
    dispatcher: CoroutineDispatcher,
) : PlayerRepository.EventHandler, LobbyRepository.EventHandler {
    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    private var serverJob: Job? = null

    init {
        lobbyRepository.eventHandler = this
        playerRepository.eventHandler = this
    }

    fun start() {
        if (serverJob != null) {
            return
        }
        serverJob = startServerJob(scope)
    }

    fun stop() {
        serverJob?.cancel()
        serverJob = null
    }

    private fun startServerJob(scope: CoroutineScope) = scope.launch {
        embeddedServer(Netty) {
            install(WebSockets)

            routing {
                webSocket {
                    val connectionId = generateUUID()
                    val newConnection = Connection(
                        connectionId,
                        this,
                    )
                    connectionRepository.registerConnection(newConnection)

                    try {
                        playerRepository.createPlayer(newConnection.playerId)
                        sendEvent(Connected(newConnection.playerId), json)
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            println("Server received: $receivedText")
                            handleEvent(
                                newConnection.playerId,
                                newConnection.session,
                                json.parseClientEvent(receivedText),
                            )
                        }
                        sendEvent(Disconnected, json)
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    } finally {
                        lobbyRepository.getLobbyForPlayer(newConnection.playerId)?.let {
                            lobbyRepository.leaveLobby(it, newConnection.playerId)
                        }
                        playerRepository.deletePlayer(newConnection.playerId)
                        connectionRepository.deregisterConnection(newConnection)
                    }
                }
            }
        }.start(wait = true)
    }

    private suspend fun handleEvent(
        playerId: String,
        session: DefaultWebSocketSession,
        clientEvent: ClientEvent,
    ) {
        when (clientEvent) {
            is CreateLobby -> {
                val newLobby = lobbyRepository.createLobby(clientEvent.lobbyName)
                lobbyRepository.joinLobby(newLobby.id, playerId)
                session.sendEvent(LobbyCreatedFromRequest(newLobby), json)
            }
            is JoinLobby -> {
                if (!lobbyRepository.joinLobby(clientEvent.lobbyId, playerId)) {
                    return
                }
                val players = lobbyRepository.getLobby(clientEvent.lobbyId)?.players?.mapNotNull {
                    playerRepository.getPlayer(it)
                } ?: emptyList()
                session.sendEvent(PlayerListFromRequest(players), json)
            }
            is DeleteLobby -> {
                val lobbyId = lobbyRepository.getLobbyForPlayer(playerId) ?: return
                lobbyRepository.deleteLobby(lobbyId)
            }
            is StartGame -> {
                handleStartGameEvent(playerId)
            }
            is SetPlayerName -> {
                playerRepository.updatePlayer(playerId, clientEvent.playerName)
            }
            Ping -> return
            is LeaveLobby -> {
                val leavingPlayer = playerRepository.getPlayer(playerId) ?: return
                val lobbyId = lobbyRepository.getLobbyForPlayer(playerId) ?: return
                lobbyRepository.leaveLobby(lobbyId, playerId)
                session.sendEvent(LeftLobby(leavingPlayer), json)
            }
            ListPlayers -> {
                val lobbyId = lobbyRepository.getLobbyForPlayer(playerId) ?: return
                val lobby = lobbyRepository.getLobby(lobbyId) ?: return
                val players = lobby.players.mapNotNull {
                    playerRepository.getPlayer(it)
                }
                session.sendEvent(PlayerListFromRequest(players), json)
            }
            is SetReadyToStartGame -> {
                playerRepository.setPlayerReady(playerId, clientEvent.isReady)
            }
            is GamePlayerIntent -> {
                handlePlayerIntent(playerId, clientEvent.playerIntent)
            }
        }
    }

    private fun handlePlayerIntent(playerId: String, playerIntent: PlayerIntent) {
        val lobbyId = lobbyRepository.getLobbyForPlayer(playerId) ?: return
        val game = gameRepository.getGame(lobbyId) ?: return

        game.onPlayerIntentReceived(playerId, playerIntent)
    }

    private suspend fun handleStartGameEvent(playerId: String) {
        val lobbyId = lobbyRepository.getLobbyForPlayer(playerId) ?: return
        val lobby = lobbyRepository.getLobby(lobbyId) ?: return

        val playerReadyCount = lobby.players.count {
            val player = playerRepository.getPlayer(it)
            player?.readyToStart == true
        }

        if (playerReadyCount < lobby.players.size) {
            println("All users are not yet ready. Game cannot start")
            return
        }

        val createdGame = gameRepository.createGame(lobby.id) ?: return

        broadcastToLobby(lobby.id, GameStarted)
        createdGame.registerServerEventHandler(object : MultiplayerGameEventHandler {
            override fun onStateChangeExecuted(change: StateChange) {
                scope.launch {
                    broadcastToLobby(lobbyId, GameChange(change))
                }
            }
        })

        broadcastToLobby(lobby.id, GameStateMessage(createdGame.gameState))
    }

    suspend fun broadcastToLobby(lobbyId: String, serverEvent: ServerEvent) {
        lobbyRepository.getLobby(lobbyId)?.players?.forEach {
            connectionRepository.getConnection(it)?.session?.sendEvent(serverEvent, json)
        }
    }

    override suspend fun onPlayerUpdated(player: Player) {
        val lobbyId = lobbyRepository.getLobbyForPlayer(player.id)
        if (lobbyId == null) {
            connectionRepository.getConnection(player.id)?.session?.sendEvent(PlayerUpdated(player), json)
        } else {
            lobbyRepository.getLobby(lobbyId)?.let {
                broadcastToLobby(lobbyId, PlayerUpdated(player))
            }
        }
    }

    override suspend fun onPlayerJoined(playerId: String, lobbyId: String) {
        val player = playerRepository.getPlayer(playerId) ?: return
        lobbyRepository.getLobby(lobbyId)?.let {
            broadcastToLobby(lobbyId, JoinedLobby(lobbyId, player))
        }
    }

    override suspend fun onPlayerLeft(playerId: String, lobbyId: String) {
        val player = playerRepository.getPlayer(playerId) ?: return
        lobbyRepository.getLobby(lobbyId)?.let {
            broadcastToLobby(lobbyId, LeftLobby(player))
        }
    }

    private suspend inline fun <reified T : ServerEvent> DefaultWebSocketSession.sendEvent(event: T, json: Json) {
        val message = json.createSerializedMessage(event)
        println("Server send: $message")
        send(message)
    }
}
