package com.cramsan.stranded.server.demoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Composable function that generates a screen that displays the state of the client and server, as well as
 * provides controls to make changes to them.
 *
 * @author cramsan
 */
@Suppress("LongMethod", "FunctionNaming")
@Composable
fun DemoGameScreen(
    playerName: String,
    lobbyName: String,
    lobbyId: String,
    gameContent: String,
    viewModel: DemoGameViewModel,
) {
    Column {
        Row {
            Button(
                onClick = { viewModel.onStartServerSelected() }
            ) {
                Text("Start Server")
            }
            Button(
                onClick = { viewModel.onStopServerSelected() }
            ) {
                Text("Stop Server")
            }
        }
        Row {
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = playerName,
            label = { Text(text = "Player name") },
            onValueChange = { viewModel.onPlayerNameUpdated(it) }
        )
        Row {
            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = lobbyName,
                    label = { Text(text = "Lobby name") },
                    onValueChange = { viewModel.onLobbyNameUpdated(it) }
                )
                Row {
                    Button(
                        onClick = { viewModel.onCreateLobbySelected() }
                    ) {
                        Text("Create Lobby")
                    }
                    Button(
                        onClick = { viewModel.onListPlayersSelected() }
                    ) {
                        Text("List Players")
                    }
                }
            }
        }
        Column {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = lobbyId,
                label = { Text(text = "Lobby Id") },
                onValueChange = { viewModel.onLobbyIdUpdated(it) }
            )
            Row {
                Button(
                    onClick = { viewModel.onJoinLobbySelected() }
                ) {
                    Text("Join Lobby")
                }
                Button(
                    onClick = { viewModel.onLeaveLobbySelected() }
                ) {
                    Text("Leave Lobby")
                }
                Button(
                    onClick = { viewModel.onDeleteLobbySelected() }
                ) {
                    Text("Delete Lobby")
                }
            }
        }
        Row {
            Button(
                onClick = { viewModel.onSetReadySelected() }
            ) {
                Text("Set Ready=True")
            }
            Button(
                onClick = { viewModel.onSetNotReadySelected() }
            ) {
                Text("Set Ready=False")
            }
            Button(
                onClick = { viewModel.onStartGameSelected() }
            ) {
                Text("Start Game")
            }
        }
        Button(
            onClick = { viewModel.onGameActionSelected() }
        ) {
            Text("Game action")
        }
        Text(gameContent)
    }
}
