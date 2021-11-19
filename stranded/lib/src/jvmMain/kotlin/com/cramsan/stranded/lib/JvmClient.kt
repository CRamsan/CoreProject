package com.cramsan.stranded.lib

import com.cramsan.stranded.lib.client.Client
import com.cramsan.stranded.lib.client.ClientEventHandler
import com.cramsan.stranded.lib.messages.ClientEvent
import com.cramsan.stranded.lib.messages.Connected
import com.cramsan.stranded.lib.messages.JoinedLobby
import com.cramsan.stranded.lib.messages.LeftLobby
import com.cramsan.stranded.lib.messages.PlayerUpdated
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.messages.createSerializedClientMessage
import com.cramsan.stranded.lib.messages.parseServerEvent
import com.cramsan.stranded.lib.repository.Player
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class JvmClient : Client {
    override var scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var client: HttpClient
    lateinit var clientJob: Job

    lateinit var defaultClientWebSocketSession: DefaultWebSocketSession

    override val listeners = mutableListOf<ClientEventHandler>()

    override var player = Player("", "", false)
        private set

    override var lobbyId: String? = null
        private set

    override fun start() {
        client = HttpClient(CIO) {
            install(WebSockets)
        }
        clientJob = scope.launch {
            // Wait two seconds while the server starts
            delay(2000)
            client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 80) {
                defaultClientWebSocketSession = this
                val messageOutputRoutine = launch { outputMessages() }
                messageOutputRoutine.join()
            }
        }
    }

    override fun stop() {
        client.close()
    }

    override fun registerListener(eventHandler: ClientEventHandler) {
        listeners.add(eventHandler)
    }

    override fun deregisterListener(eventHandler: ClientEventHandler) {
        listeners.remove(eventHandler)
    }

    override fun sendMessage(clientEvent: ClientEvent) {
        scope.launch {
            try {
                val message = createSerializedClientMessage(clientEvent)
                println("Client send: $message")
                defaultClientWebSocketSession.send(message)
            } catch (e: Exception) {
                println("Error while sending: " + e.localizedMessage)
            }
        }
    }

    override suspend fun outputMessages() {
        try {
            for (message in defaultClientWebSocketSession.incoming) {
                message as? Frame.Text ?: continue
                val receivedText = message.readText()
                println("Client received: $receivedText")
                handleServerEvent(parseServerEvent(receivedText))
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
        }
    }

    override fun handleServerEvent(event: ServerEvent) {
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
