package com.cramsan.stranded.server

import com.cramsan.stranded.server.game.ClientEventHandler
import com.cramsan.stranded.server.messages.ClientEvent
import com.cramsan.stranded.server.messages.Connected
import com.cramsan.stranded.server.messages.JoinedLobby
import com.cramsan.stranded.server.messages.LeftLobby
import com.cramsan.stranded.server.messages.PlayerUpdated
import com.cramsan.stranded.server.messages.ServerEvent
import com.cramsan.stranded.server.messages.createSerializedClientMessage
import com.cramsan.stranded.server.messages.parseServerEvent
import com.cramsan.stranded.server.repository.Player
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CommonClient(
    private val json: Json,
    dispatcher: CoroutineDispatcher,
) : Client {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    private var clientJob: Job? = null

    private var defaultClientWebSocketSession: DefaultWebSocketSession? = null

    private val listeners = mutableListOf<ClientEventHandler>()

    override var player = Player.disconnectedPlayer
        private set

    override var lobbyId: String? = null
        private set

    override fun isConnected() = player.id.isNotEmpty()

    override fun start() {
        if (clientJob != null) {
            return
        }
        clientJob = scope.launch {
            client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 80) {
                defaultClientWebSocketSession = this
                val messageOutputRoutine = launch { listenForIncomingMessages(this@webSocket) }
                messageOutputRoutine.join()
            }
        }
    }

    override fun stop() {
        client.close()
        clientJob?.cancel()
        clientJob = null
    }

    override fun registerListener(eventHandler: ClientEventHandler) {
        listeners.add(eventHandler)
    }

    override fun deregisterListener(eventHandler: ClientEventHandler) {
        listeners.remove(eventHandler)
    }

    override fun sendMessage(clientEvent: ClientEvent) {
        val session = defaultClientWebSocketSession
        requireNotNull(session)
        scope.launch {
            try {
                val message = json.createSerializedClientMessage(clientEvent)
                println("Client send: $message")
                session.send(message)
            } catch (e: Throwable) {
                println("Error while sending: " + e.message)
            }
        }
    }

    private suspend fun listenForIncomingMessages(session: DefaultWebSocketSession) {
        try {
            for (message in session.incoming) {
                val textMessage = message as? Frame.Text ?: continue
                val receivedText = textMessage.readText()
                println("Client received: $receivedText")
                handleServerEvent(json.parseServerEvent(receivedText))
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.message)
        }
    }

    private fun handleServerEvent(event: ServerEvent) {
        when (event) {
            is Connected -> {
                player = Player(
                    event.playerId,
                    "",
                    false,
                )
            }
            is PlayerUpdated -> {
                player = Player(
                    event.player.id,
                    event.player.name,
                    event.player.readyToStart,
                )
            }
            is JoinedLobby -> {
                lobbyId = event.lobbyId
            }
            is LeftLobby -> {
                lobbyId = null
            }
            else -> Unit
        }
        listeners.forEach { it.onServerEventReceived(event) }
    }
}
