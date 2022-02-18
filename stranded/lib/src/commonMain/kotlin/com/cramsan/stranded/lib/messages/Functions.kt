package com.cramsan.stranded.lib.messages

import com.cramsan.stranded.lib.game.logic.MutableStrandedGameState
import com.cramsan.stranded.lib.game.logic.StrandedGameState
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val module = SerializersModule {
}
private val serializer = Json {
    serializersModule = module
}
