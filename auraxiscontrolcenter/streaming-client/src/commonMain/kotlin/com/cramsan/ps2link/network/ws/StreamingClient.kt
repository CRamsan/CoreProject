package com.cramsan.ps2link.network.ws

import com.cramsan.framework.logging.logE
import com.cramsan.framework.logging.logI
import com.cramsan.framework.logging.logV
import com.cramsan.framework.logging.logW
import com.cramsan.ps2link.network.ws.messages.ClientCommand
import com.cramsan.ps2link.network.ws.messages.ServerEvent
import com.cramsan.ps2link.network.ws.messages.createSerializedClientMessage
import com.cramsan.ps2link.network.ws.messages.parseServerEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
    private val overrideUrl: String? = null,
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
            logW(TAG, "Client already running. Ignoring request to start")
            return
        }
        clientJob = scope.launch {
            val endpointUrl = overrideUrl ?: DEFAULT_URL
            client.webSocket("$endpointUrl?environment=$environment&service-id=s:$serviceId") {
                defaultClientWebSocketSession = this
                val messageOutputRoutine = launch {
                    listenForIncomingMessages(this@webSocket)
                }
                messageOutputRoutine.join()
                close()
            }
        }
    }

    /**
     * Close the client and any current activity.
     */
    fun stop() {
        logI(TAG, "Closing websocket session")
        val job = clientJob
        clientJob = null
        job?.cancel()
        logI(TAG, "Websocket session closed")
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
            logW(TAG, "Cannot send command. Client is not running.")
            return
        }

        scope.launch {
            try {
                val message = json.createSerializedClientMessage(clientEvent)
                logI(TAG, "Sending message of type ${clientEvent::class.simpleName}")
                logV(TAG, "Client raw message: $message")
                session.send(message)
            } catch (e: Throwable) {
                logE(TAG, "Error sending message. Error: ${e.message}", e)
            }
        }
    }

    /**
     * Send the [rawMessage] to the WS API.
     */
    fun sendRawMessage(rawMessage: String) {
        val session = defaultClientWebSocketSession
        if (session == null) {
            logW(TAG, "Cannot send command. Client is not running.")
            return
        }

        scope.launch {
            try {
                logV(TAG, "Client raw message: $rawMessage")
                session.send(rawMessage)
            } catch (e: Throwable) {
                logE(TAG, "Error sending message. Error: ${e.message}", e)
            }
        }
    }

    private suspend fun listenForIncomingMessages(
        session: DefaultWebSocketSession,
    ) {
        for (message in session.incoming) {
            message as? Frame.Text ?: continue
            val receivedText = message.readText()
            logI(TAG, "Received message from server")
            logV(TAG, "Server raw message: $receivedText")

            if (receivedText == HELP_MESSAGE) {
                continue
            }
            try {
                val event = json.parseServerEvent(receivedText)
                handleServerEvent(event)
            } catch (t: Throwable) {
                logE(TAG, "Error sending message. Error: ${t.message}", t)
            }
        }
    }

    private fun handleServerEvent(event: ServerEvent) {
        listeners.forEach { it.onServerEventReceived(event) }
    }

    companion object {
        const val TAG = "StreamingClient"

        const val DEFAULT_URL = "wss://push.planetside2.com/streaming"

        const val HELP_MESSAGE = "{\"send this for help\":{\"service\":\"event\",\"action\":\"help\"}}"
    }
}
