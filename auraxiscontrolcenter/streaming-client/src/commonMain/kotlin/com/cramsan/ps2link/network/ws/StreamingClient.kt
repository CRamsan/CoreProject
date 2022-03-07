package com.cramsan.ps2link.network.ws

import com.cramsan.ps2link.network.ws.messages.ClientCommand
import com.cramsan.ps2link.network.ws.messages.ServerEvent
import com.cramsan.ps2link.network.ws.messages.createSerializedClientMessage
import com.cramsan.ps2link.network.ws.messages.parseServerEvent
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * This client accesses the Streaming(WebSocket API) for the Census API. The [client] needs to have
 * the [WebSockets] feature enabled. The [json] instance needs to have a [SerializersModule] with a
 * classDiscriminator set to something other than "type". The [serviceId] will be used to identify the
 * origin client. The [environment] to determine which namespace we will be connected to.
 *
 * @see Census API Docs - http://census.daybreakgames.com/]
 * @author cramsan
 */
class StreamingClient(
    private val client: HttpClient,
    private val json: Json,
    private val serviceId: String,
    private val environment: Environment,
    dispatcher: CoroutineDispatcher,
) {
    private var scope = CoroutineScope(SupervisorJob() + dispatcher)

    private var clientJob: Job? = null

    private var defaultClientWebSocketSession: DefaultWebSocketSession? = null

    private val listeners = mutableListOf<StreamingClientEventHandler>()

    /**
     * Start the client and connect to the WebSocket server.
     */
    fun start() {
        if (clientJob != null) {
            return
        }
        clientJob = scope.launch {
            client.webSocket("wss://push.planetside2.com/streaming?environment=$environment&service-id=s:$serviceId") {
                defaultClientWebSocketSession = this
                val messageOutputRoutine = launch { listenForIncomingMessages(this@webSocket) }
                messageOutputRoutine.join()
                println("Closing websocket session")
                close()
            }
        }
    }

    /**
     * Close the client and any current activity.
     */
    fun stop() {
        client.close()
        clientJob?.cancel()
        clientJob = null
    }

    /**
     * Register [eventHandler] to receive events from this client.
     */
    fun registerListener(eventHandler: StreamingClientEventHandler) {
        listeners.add(eventHandler)
    }

    /**
     * Deregister the [eventHandler] to stop receiving events from this client.
     */
    fun deregisterListener(eventHandler: StreamingClientEventHandler) {
        listeners.remove(eventHandler)
    }

    /**
     * Send the [clientEvent] to the WS API.
     */
    fun sendMessage(clientEvent: ClientCommand) {
        val session = defaultClientWebSocketSession
        if (session == null) {
            // TODO: Log error
            return
        }
        scope.launch {
            try {
                val message = json.createSerializedClientMessage(clientEvent)
                println("Client sending: $message")
                session.send(message)
            } catch (e: Throwable) {
                println("Error sending message. Error: ${e.message}")
            }
        }
    }

    private suspend fun listenForIncomingMessages(session: DefaultWebSocketSession) {
        for (message in session.incoming) {
            message as? Frame.Text ?: continue
            val receivedText = message.readText()
            println("Client received: $receivedText")
            try {
                handleServerEvent(json.parseServerEvent(receivedText))
            } catch (e: SerializationException) {
                handleServerEventString(receivedText)
            } catch (t: Throwable) {
                println("Error receiving message. Error: ${t.message}")
            }
        }
    }

    private fun handleServerEvent(event: ServerEvent) {
        listeners.forEach { it.onServerEventReceived(event) }
    }

    private fun handleServerEventString(event: String) {
        listeners.forEach { it.onUnhandledServerEventReceived(event) }
    }
}
