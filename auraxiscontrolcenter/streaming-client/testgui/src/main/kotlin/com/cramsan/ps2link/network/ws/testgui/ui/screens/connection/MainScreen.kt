package com.cramsan.ps2link.network.ws.testgui.ui.screens.connection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.census.DBGServiceClient
import com.cramsan.ps2link.network.ws.testgui.application.ApplicationManager
import com.cramsan.ps2link.network.ws.testgui.ui.screens.BaseScreen

/**
 * Main screen that is first displayed to the user.
 */
class MainScreen(
    private val applicationManager: ApplicationManager,
    private val serviceClient: DBGServiceClient,
    private val dispatcherProvider: DispatcherProvider,
) : BaseScreen<MainViewModel>() {
    override fun createViewModel(): MainViewModel {
        return MainViewModel(applicationManager, serviceClient, dispatcherProvider)
    }

    @Composable
    override fun ScreenContent(viewModel: MainViewModel) {
        MainScreenContent(viewModel)
    }
}

@Composable
private fun MainScreenContent(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column {
        Button(
            onClick = { viewModel.openSettings() },
        ) {
            Text("Settings")
        }

        TextField(
            value = uiState.characterName,
            label = { Text(text = "Enter a player name") },
            onValueChange = { viewModel.updateCharacterName(it) },
        )

        LazyColumn(
            modifier = Modifier.height(300.dp),
        ) {
            items(uiState.playerSuggestions) {
                Text(
                    it.name ?: "",
                    modifier = Modifier.clickable { viewModel.selectCharacter(it) },
                )
            }
        }

        uiState.selectedPlayer?.let {
            Text(it.name ?: "")

            Text("BR: ${it.battleRank}")

            Button(
                onClick = { viewModel.startListening() },
            ) {
                Text("Start")
            }

            Button(
                onClick = { viewModel.stopListening() },
            ) {
                Text("Stop")
            }
        }
    }
}
