package com.cramsan.ps2link.network.ws.testgui.ui.screens.connection

import com.cramsan.ps2link.core.models.Character
import kotlinx.collections.immutable.ImmutableList

/**
 * Data class to represent the MainScreen.
 */
data class MainUIState(
    val isRunning: Boolean,
    val characterName: String,
    val playerSuggestions: ImmutableList<Character>,
    val selectedPlayer: Character?,
    val isListLoading: Boolean,
    val isLoading: Boolean,
    val isError: Boolean,
)
