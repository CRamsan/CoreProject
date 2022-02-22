package com.cramsan.stranded.lib.messages

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val module = SerializersModule {
}
private val serializer = Json {
    serializersModule = module
}
