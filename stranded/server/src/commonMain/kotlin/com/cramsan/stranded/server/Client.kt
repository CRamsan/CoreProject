package com.cramsan.stranded.server

import com.cramsan.stranded.server.game.ClientEventHandler
import com.cramsan.stranded.server.messages.ClientEvent
import com.cramsan.stranded.server.repository.Player

/**
 * @author cramsan
 */
interface Client {
    val player: Player
    val lobbyId: String?
    fun isConnected(): Boolean
    fun start()
    fun stop()
    fun registerListener(eventHandler: ClientEventHandler)
    fun deregisterListener(eventHandler: ClientEventHandler)
    fun sendMessage(clientEvent: ClientEvent)
}
