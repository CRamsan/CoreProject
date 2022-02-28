package com.cramsan.ps2link.network.ws.testgui

import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.StreamingClientEventHandler
import com.cramsan.ps2link.network.ws.messages.CharacterSubscribe
import com.cramsan.ps2link.network.ws.messages.ClearSubscribe
import com.cramsan.ps2link.network.ws.messages.ClientCommand
import com.cramsan.ps2link.network.ws.messages.Echo
import com.cramsan.ps2link.network.ws.messages.EventType
import com.cramsan.ps2link.network.ws.messages.Help
import com.cramsan.ps2link.network.ws.messages.ServerEvent
import com.cramsan.ps2link.network.ws.messages.WorldSubscribe
import com.cramsan.ps2link.network.ws.messages.createSerializedClientMessage
import com.cramsan.ps2link.network.ws.messages.createSerializedMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json

/**
 * ViewModel that powers the UI of this demo application. This viewModel has an action for pretty much all possible
 * server and client events.
 *
 * @author cramsan
 */
class ConnectionViewModel(
    private val client: StreamingClient,
    private val json: Json,
) : StreamingClientEventHandler {

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        client.registerListener(this)
    }

    private val eventsSelected = EventType.values().associateWith {
        false
    }.toMutableMap()

    private val _connected = MutableStateFlow(false)
    private val _command = MutableStateFlow<ClientCommand?>(null)
    private val _events = MutableStateFlow(emptyList<String>())
    private val _charactersField = MutableStateFlow("")
    private val _worldsField = MutableStateFlow("")

    private val characters = _charactersField.map { field ->
        field.split(",")
            .map { section -> section.trim() }
    }.stateIn(scope, SharingStarted.Lazily, emptyList())

    private val worlds = _worldsField.map { field ->
        field.split(",")
            .map { section -> section.trim() }
    }.stateIn(scope, SharingStarted.Lazily, emptyList())

    val connected: StateFlow<Boolean> = _connected
    val commandText: Flow<String> = _command.map { it?.let { command -> json.createSerializedClientMessage(command) } ?: "" }
    val events: StateFlow<List<String>> = _events
    val charactersField: StateFlow<String> = _charactersField
    val worldsField: StateFlow<String> = _worldsField

    init {
        characters.onEach {
            updateCommand()
        }.launchIn(scope)
        worlds.onEach {
            updateCommand()
        }.launchIn(scope)
    }

    private fun updateCommand() {
        val command = _command.value
        _command.value = when (command) {
            is CharacterSubscribe -> command.copy(
                characters = characters.value,
                eventNames = selectedEventTypes()
            )
            is WorldSubscribe -> command.copy(
                worlds = worlds.value,
                eventNames = selectedEventTypes()
            )
            is ClearSubscribe -> command.copy(
                characters = characters.value,
                worlds = worlds.value,
                eventNames = selectedEventTypes()
            )
            else -> command
        }
    }

    fun onStartClientSelected() {
        client.start()
        _connected.value = true
    }

    fun onStopClientSelected() {
        client.stop()
        _connected.value = false
    }

    override fun onServerEventReceived(serverEvent: ServerEvent) {
        val eventString = json.createSerializedMessage(serverEvent)
        _events.value = _events.value.let {
            if (it.size < 100) {
                it + eventString
            } else {
                it.subList(0, it.lastIndex - 1) + eventString
            }
        }
    }

    override fun onUnhandledServerEventReceived(serverEventString: String) {
        _events.value = _events.value.let {
            if (it.size < 100) {
                it + serverEventString
            } else {
                it.subList(0, it.lastIndex - 1) + serverEventString
            }
        }
    }

    fun onSendCommandSelected() {
        _command.value.let {
            if (it == null) {
                println("Command was null")
            } else {
                client.sendMessage(it)
            }
        }
    }

    fun onCharactersTextUpdated(value: String) {
        _charactersField.value = value
    }

    fun onWorldsTextUpdated(value: String) {
        _worldsField.value = value
    }

    fun onEventTypeSelected(event: EventType, selected: Boolean) {
        eventsSelected[event] = selected
        updateCommand()
    }

    fun onEchoCommandSelected() {
        _command.value = Echo()
    }

    fun onHelpCommandSelected() {
        _command.value = Help()
    }

    fun onCharacterSubscribeCommandSelected() {
        _command.value = CharacterSubscribe(emptyList(), emptyList())
        updateCommand()
    }

    fun onWorldSubscribeCommandSelected() {
        _command.value = WorldSubscribe(emptyList(), emptyList())
        updateCommand()
    }

    fun onClearSubscribeCommandSelected() {
        _command.value = ClearSubscribe(emptyList(), emptyList(), emptyList())
        updateCommand()
    }

    private fun selectedEventTypes(): List<EventType> = eventsSelected.filter { it.value }.map { it.key }
}
