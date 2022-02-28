package com.cramsan.ps2link.network.ws.testgui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cramsan.ps2link.network.ws.messages.EventType

@Suppress("LongMethod", "FunctionNaming")
@Composable
fun ConnectionScreen(
    connected: Boolean,
    commandText: String,
    charactersField: String,
    worldsField: String,
    events: List<String>,
    viewModel: ConnectionViewModel,
) {
    Row {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                val state = if (connected) {
                    "connected"
                } else {
                    "disconnected"
                }
                Text(
                    "Status: $state",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Button(
                    onClick = { viewModel.onStartClientSelected() }
                ) {
                    Text("Start Client")
                }
                Button(
                    onClick = { viewModel.onStopClientSelected() }
                ) {
                    Text("Stop Client")
                }
            }
            Text(commandText)
            TextField(
                value = charactersField,
                label = { Text(text = "List of character Ids") },
                onValueChange = { viewModel.onCharactersTextUpdated(it) }
            )
            TextField(
                value = worldsField,
                label = { Text(text = "List of world Ids") },
                onValueChange = { viewModel.onWorldsTextUpdated(it) }
            )
            Column {
                Text("Commands:")
                Row {
                    Button(
                        onClick = { viewModel.onHelpCommandSelected() }
                    ) {
                        Text("Help")
                    }
                    Button(
                        onClick = { viewModel.onEchoCommandSelected() }
                    ) {
                        Text("Echo")
                    }
                    Button(
                        onClick = { viewModel.onCharacterSubscribeCommandSelected() }
                    ) {
                        Text("CharacterSubscribe")
                    }
                }
                Row {
                    Button(
                        onClick = { viewModel.onWorldSubscribeCommandSelected() }
                    ) {
                        Text("WorldSubscribe")
                    }
                    Button(
                        onClick = { viewModel.onClearSubscribeCommandSelected() }
                    ) {
                        Text("ClearSubscribe")
                    }
                }
            }
            Column {
                EventType.values().forEach { event ->
                    Row {
                        val checkedState = remember { mutableStateOf(false) }
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = {
                                checkedState.value = it
                                viewModel.onEventTypeSelected(event, it)
                            }
                        )
                        Text(
                            event.name,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
            Button(
                onClick = { viewModel.onSendCommandSelected() }
            ) {
                Text("Send command")
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            items(events) {
                Text(
                    it,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}
