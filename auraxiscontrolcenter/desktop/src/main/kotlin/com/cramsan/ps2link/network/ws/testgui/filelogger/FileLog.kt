package com.cramsan.ps2link.network.ws.testgui.filelogger

import com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker.PlayerEvent
import kotlinx.coroutines.flow.Flow

interface FileLog {

    fun getLines(lines: Int): List<PlayerEvent>

    fun observeLines(): Flow<PlayerEvent>

    suspend fun addLine(event: PlayerEvent)
}
