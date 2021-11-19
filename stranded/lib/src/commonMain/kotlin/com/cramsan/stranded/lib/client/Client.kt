package com.cramsan.stranded.lib.client

import com.cramsan.stranded.lib.messages.ClientEvent
import com.cramsan.stranded.lib.messages.ServerEvent
import com.cramsan.stranded.lib.repository.Player
import kotlinx.coroutines.CoroutineScope

interface Client {
    var scope: CoroutineScope

    val listeners: MutableList<ClientEventHandler>

    val player: Player

    val lobbyId: String?

    fun start()

    fun stop()

    fun registerListener(eventHandler: ClientEventHandler)

    fun deregisterListener(eventHandler: ClientEventHandler)

    fun sendMessage(clientEvent: ClientEvent)

    suspend fun outputMessages()

    fun handleServerEvent(event: ServerEvent)
}
