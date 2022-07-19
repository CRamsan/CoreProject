package com.cramsan.stranded.server

import io.ktor.websocket.DefaultWebSocketSession

class Connection(
    val playerId: String,
    val session: DefaultWebSocketSession,
)
