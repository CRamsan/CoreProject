package com.cramsan.stranded.lib

import com.cramsan.stranded.lib.game.intent.PlayerIntent
import com.cramsan.stranded.lib.game.logic.GameEventHandler
import com.cramsan.stranded.lib.game.models.common.Card
import com.cramsan.stranded.lib.game.models.common.Equippable
import com.cramsan.stranded.lib.game.models.common.StartingFood
import com.cramsan.stranded.lib.game.models.common.Status
import com.cramsan.stranded.lib.game.models.night.NightEvent
import com.cramsan.stranded.lib.game.models.scavenge.Resource
import com.cramsan.stranded.lib.game.models.scavenge.ResourceType
import com.cramsan.stranded.lib.game.models.state.Change
import com.cramsan.stranded.lib.messages.ClientEvent
import com.cramsan.stranded.lib.messages.Connected
import com.cramsan.stranded.lib.messages.CreateLobby
import com.cramsan.stranded.lib.messages.DeleteLobby
import com.cramsan.stranded.lib.messages.Disconnected
import com.cramsan.stranded.lib.messages.GameChange
import com.cramsan.stranded.lib.messages.GamePlayerIntent
import com.cramsan.stranded.lib.messages.GameStarted
import com.cramsan.stranded.lib.messages.GameStateMessage
import com.cramsan.stranded.lib.messages.JoinLobby
import com.cramsan.stranded.lib.messages.JoinedLobby
import com.cramsan.stranded.lib.messages.LeaveLobby
import com.cramsan.stranded.lib.messages.LeftLobby
import com.cramsan.stranded.lib.messages.ListLobbies
import com.cramsan.stranded.lib.messages.ListPlayers
import com.cramsan.stranded.lib.messages.LobbyCreated
import com.cramsan.stranded.lib.messages.LobbyCreatedFromRequest
import com.cramsan.stranded.lib.messages.LobbyDestroyed
import com.cramsan.stranded.lib.messages.LobbyList
import com.cramsan.stranded.lib.messages.Ping
import com.cramsan.stranded.lib.messages.PlayerListFromRequest
import com.cramsan.stranded.lib.messages.PlayerUpdated
import com.cramsan.stranded.lib.messages.ReadyToStartGame
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.messages.SetPlayerName
import com.cramsan.stranded.lib.messages.SetReadyToStart
import com.cramsan.stranded.lib.messages.StartGame
import com.cramsan.stranded.lib.messages.createSerializedMessage
import com.cramsan.stranded.lib.messages.parseClientEvent
import com.cramsan.stranded.lib.repository.ConnectionRepository
import com.cramsan.stranded.lib.repository.GameRepository
import com.cramsan.stranded.lib.repository.Lobby
import com.cramsan.stranded.lib.repository.LobbyRepository
import com.cramsan.stranded.lib.repository.Player
import com.cramsan.stranded.lib.repository.PlayerRepository
import com.cramsan.stranded.lib.utils.generateUUID
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class Server {
    var scope: CoroutineScope? = null

    lateinit var lobbyRepository: LobbyRepository

    lateinit var playerRepository: PlayerRepository

    lateinit var gameRepository: GameRepository

    lateinit var connectionRepository: ConnectionRepository

    lateinit var serverJob: Job

    fun start() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        playerRepository = PlayerRepository(object : PlayerRepository.EventHandler {
            override suspend fun onPlayerUpdated(player: Player) {
                val lobby = lobbyRepository.getLobbyForPlayer(player.id)
                if (lobby == null) {
                    connectionRepository.getConnection(player.id).session.sendEvent(PlayerUpdated(player))
                } else {
                    connectionRepository.broadcastToLobby(lobby.id, PlayerUpdated(player))
                }
            }
        })

        lobbyRepository = LobbyRepository(
            playerRepository,
            object : LobbyRepository.EventHandler {
                override suspend fun onLobbyCreated(lobby: Lobby) {
                    connectionRepository.broadcastToAll(LobbyCreated(lobby))
                }
                override suspend fun onLobbyDestroyed(lobbyId: String) {
                    connectionRepository.broadcastToAll(LobbyDestroyed(lobbyId))
                }
                override suspend fun onPlayerJoined(playerId: String, lobbyId: String) {
                    val player = playerRepository.getPlayer(playerId)

                    connectionRepository.broadcastToLobby(lobbyId, JoinedLobby(lobbyId, player))
                }

                override suspend fun onPlayerLeft(playerId: String, lobbyId: String) {
                    val player = playerRepository.getPlayer(playerId)

                    connectionRepository.broadcastToLobby(lobbyId, LeftLobby(lobbyId, player))
                }
            }
        )

        connectionRepository = ConnectionRepository(lobbyRepository)
        gameRepository = GameRepository(Dispatchers.IO)
        serverJob = startServerJob()
    }

    fun stop() {
        serverJob.cancel()
        requireNotNull(scope).cancel()
    }

    fun startServerJob() = requireNotNull(scope).launch {
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
                        sendEvent(Connected(newConnection.playerId))
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            println("Server received: $receivedText")
                            handleEvent(
                                newConnection.playerId,
                                newConnection.session,
                                parseClientEvent(receivedText),
                            )
                        }
                        sendEvent(Disconnected)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        lobbyRepository.getLobbyForPlayer(newConnection.playerId)?.let {
                            lobbyRepository.leaveLobby(it.id, newConnection.playerId)
                        }
                        playerRepository.deletePlayer(newConnection.playerId)
                        connectionRepository.deregisterConnection(newConnection)
                    }
                }
            }
        }.start(wait = true)
    }

    suspend fun handleEvent(
        playerId: String,
        session: DefaultWebSocketSession,
        clientEvent: ClientEvent,
    ) {
        when (clientEvent) {
            is CreateLobby -> {
                val newLobby = lobbyRepository.createLobby(clientEvent.lobbyName)
                session.sendEvent(LobbyCreatedFromRequest(newLobby))
            }
            is JoinLobby -> {
                lobbyRepository.joinLobby(clientEvent.lobbyId, playerId)
            }
            ListLobbies -> {
                session.sendEvent(LobbyList(lobbyRepository.getAllLobbies()))
            }
            is DeleteLobby -> {
                // TODO: This is not enabled as we do not have a way to prevent users from deleting all lobbies.
                // lobbyRepository.deleteLobby(clientEvent.lobbyId)
            }
            is StartGame -> {
                val lobby = lobbyRepository.getLobbyForPlayer(playerId)
                requireNotNull(lobby)

                val playersReady = lobby.players.map {
                    val player = playerRepository.getPlayer(it)
                    player.readyToStart
                }

                // TODO: This should be updated to 2
                require(playersReady.size > 0)
                if (playersReady.filter { it }.size != lobby.players.size) {
                    println("All users are not yet ready. Game cannot start")
                    return
                }

                startGame(lobby.id, lobbyRepository, gameRepository, playerRepository)
                connectionRepository.broadcastToLobby(lobby.id, GameStarted)
            }
            is SetPlayerName -> {
                playerRepository.updatePlayer(playerId, clientEvent.playerName)
            }
            Ping -> return
            is LeaveLobby -> {
                val lobby = lobbyRepository.getLobbyForPlayer(playerId)
                requireNotNull(lobby)
                lobbyRepository.leaveLobby(lobby.id, playerId)
            }
            ListPlayers -> {
                val lobby = lobbyRepository.getLobbyForPlayer(playerId)
                requireNotNull(lobby)

                val players = lobby.players.map {
                    playerRepository.getPlayer(it)
                }
                session.sendEvent(PlayerListFromRequest(players))
            }
            ReadyToStartGame -> {
                handlePlayerIsReady(playerId)
            }
            is SetReadyToStart -> {
                handleSetPlayerReadyToStart(playerId, clientEvent.readyToStart)
            }
            is GamePlayerIntent -> {
                handlePlayerIntent(playerId, clientEvent.playerIntent)
            }
        }
    }

    private suspend fun handleSetPlayerReadyToStart(playerId: String, readyToStart: Boolean) {
        playerRepository.setPlayerReady(playerId, readyToStart)
    }

    private fun handlePlayerIntent(playerId: String, playerIntent: PlayerIntent) {
        val lobby = lobbyRepository.getLobbyForPlayer(playerId)
        requireNotNull(lobby)

        val game = gameRepository.getGame(lobby.id)
        requireNotNull(scope).launch {
            game.playerIntents.getValue(playerId).send(playerIntent)
        }
    }

    private suspend fun handlePlayerIsReady(playerId: String) {
        val lobby = lobbyRepository.getLobbyForPlayer(playerId)
        requireNotNull(lobby)

        val gameIsReady = gameRepository.setPlayerReady(playerId, lobby.id)

        if (gameIsReady) {
            val game = gameRepository.getGame(lobby.id)

            connectionRepository.broadcastToLobby(lobby.id, GameStateMessage(game.gameState))

            configurePlayerChannels(lobby.id)
            game.startGameJobAsync()
        }
    }

    private suspend fun configurePlayerChannels(lobbyId: String) {
        val game = gameRepository.getGame(lobbyId)
        game.gameEventHandler = object : GameEventHandler {
            override fun onEventHandled(change: Change) {
                requireNotNull(scope).launch {
                    connectionRepository.broadcastToLobby(lobbyId, GameChange(change))
                }
            }

            override fun onPlayerHealthChange(playerId: String, health: Int) = Unit

            override fun onCardReceived(playerId: String, card: Card) = Unit

            override fun onCardRemoved(playerId: String, card: Card) = Unit
        }
    }
}

suspend inline fun <reified T : ServerEvent> DefaultWebSocketSession.sendEvent(event: T) {
    val message = createSerializedMessage(event)
    println("Server send: $message")
    send(message)
}

fun startGame(lobbyId: String, lobbyRepository: LobbyRepository, gameRepository: GameRepository, playerRepository: PlayerRepository) {
    val lobby = lobbyRepository.getLobby(lobbyId)
    // TODO: Reenable check
    // if (lobby.players.size < 2) throw IllegalStateException("Cannot start game with less that 2 players")

    val game = gameRepository.createGame(lobbyId)

    val players = lobby.players.map {
        playerRepository.getPlayer(it)
    }

    game.configureGame(
        players,
        listOf(
            Resource(ResourceType.ROCK),
            Resource(ResourceType.STICK),
            Resource(ResourceType.FIBER),
            Resource(ResourceType.ROCK),
            Resource(ResourceType.STICK),
            Resource(ResourceType.FIBER),
            Resource(ResourceType.ROCK),
            Resource(ResourceType.STICK),
            Resource(ResourceType.FIBER),
            Resource(ResourceType.ROCK),
            Resource(ResourceType.STICK),
            Resource(ResourceType.FIBER),
        ),
        listOf(
            NightEvent("1", emptyList()),
            NightEvent("2", emptyList()),
            NightEvent("3", emptyList()),
        ),
        listOf(
            Equippable("4", 1),
            StartingFood("5", 1, 1, Status.NORMAL, 1),
        ),
    )
    gameRepository.configureGame(lobbyId)
}
