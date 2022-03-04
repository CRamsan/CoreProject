package com.cramsan.stranded.web

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text

/**
 * Composable function that generates a screen that displays the state of the client and server, as well as
 * provides controls to make changes to them.
 *
 * @author cramsan
 */
@Suppress("LongMethod", "FunctionNaming")
@Composable
fun ConnectionScreen(
    playerName: String,
    lobbyName: String,
    lobbyId: String,
    playerList: List<String>,
    viewModel: ConnectionViewModel,
) {
    Div {
        Text("Player:")
        Input(InputType.Text, attrs = {
            value(playerName)
            onInput { viewModel.onPlayerNameUpdated(it.value) }
        })
        Button(attrs = {
            onClick { viewModel.onUpdatePlayerNameSelected() }
        }) {
            Text("Update")
        }
    }

    Div {
        Text("Lobby Name:")
        Input(InputType.Text, attrs = {
            value(lobbyName)
            onInput { viewModel.onLobbyNameUpdated(it.value) }
        })
        Button(attrs = {
            onClick { viewModel.onCreateLobbySelected() }
        }) {
            Text("Create Lobby")
        }
    }

    Div {
        Text("Lobby Id:")
        Input(InputType.Text, attrs = {
            value(lobbyId)
            onInput { viewModel.onLobbyIdUpdated(it.value) }
        })
        Button(attrs = {
            onClick { viewModel.onJoinLobbySelected() }
        }) {
            Text("Join Lobby")
        }
        Button(attrs = {
            onClick { viewModel.onLeaveLobbySelected() }
        }) {
            Text("Leave Lobby")
        }
        Button(attrs = {
            onClick { viewModel.onDeleteLobbySelected() }
        }) {
            Text("Delete Lobby")
        }
    }

    Div {
        Button(attrs = {
            onClick { viewModel.onListPlayersSelected() }
        }) {
            Text("List Players")
        }
        Button(attrs = {
            onClick { viewModel.onSetReadySelected() }
        }) {
            Text("Set Ready=True")
        }
        Button(attrs = {
            onClick { viewModel.onSetNotReadySelected() }
        }) {
            Text("Set Ready=False")
        }
    }

    Div {
        Button(attrs = {
            onClick { viewModel.onStartGameSelected() }
        }) {
            Text("Start Game")
        }
        playerList.forEach {
            Text(it)
        }
    }
}
