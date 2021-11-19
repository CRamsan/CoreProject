package com.cramsan.stranded.lib.client

import com.cramsan.stranded.lib.messages.ServerEvent

fun interface ClientEventHandler {

    fun onServerEventReceived(serverEvent: ServerEvent)
}
