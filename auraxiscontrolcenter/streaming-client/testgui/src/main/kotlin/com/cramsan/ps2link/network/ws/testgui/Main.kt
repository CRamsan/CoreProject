package com.cramsan.ps2link.network.ws.testgui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cramsan.ps2link.network.ws.Environment
import com.cramsan.ps2link.network.ws.StreamingClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

/**
 * Application entry point.
 */
fun main() = application {

    /**
     * Json serialization used for the client and server communication.
     */
    val json = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            classDiscriminator = "_type"
        }
    }

    val httpClient = HttpClient {
        install(WebSockets)
    }

    /**
     * Instantiate a server and client.
     */
    val client = StreamingClient(
        httpClient,
        json,
        "PS2LinkPreProd",
        Environment.PS2,
        Dispatchers.IO,
    )

    /**
     * This is the viewModel that will power the connection UI.
     */
    val connectionViewModel = ConnectionViewModel(client, json)

    val connected: State<Boolean> = connectionViewModel.connected.collectAsState()
    val commandText: State<String> = connectionViewModel.commandText.collectAsState("")
    val charactersField: State<String> = connectionViewModel.charactersField.collectAsState()
    val worldsField: State<String> = connectionViewModel.worldsField.collectAsState()
    val events: State<List<String>> = connectionViewModel.events.collectAsState()

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            ConnectionScreen(
                connected.value,
                commandText.value,
                charactersField.value,
                worldsField.value,
                events.value,
                connectionViewModel,
            )
        }
    }
}
