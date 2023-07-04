package com.cramsan.ps2link.service

import com.cramsan.framework.assertlib.AssertUtilInterface
import com.cramsan.framework.thread.ThreadUtilInterface
import com.cramsan.ps2link.service.controller.domain.CharacterController
import com.cramsan.ps2link.service.controller.domain.WSController
import com.cramsan.ps2link.service.di.ApplicationModule
import com.cramsan.ps2link.service.di.DomainModule
import com.cramsan.ps2link.service.di.FrameworkModule
import com.cramsan.ps2link.service.service.PlayerTracker
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    startKoin {
        modules(
            FrameworkModule,
            DomainModule,
            ApplicationModule,
        )
    }

    val component = object : KoinComponent {
        val assertUtil: AssertUtilInterface by inject()
        val threadUtil: ThreadUtilInterface by inject()
        val json: Json by inject()
        val characterController: CharacterController by inject()
        val wsController: WSController by inject()
        val playerTracker: PlayerTracker by inject()
    }
    component.assertUtil
    component.threadUtil

    configureSerialization(component.json)
    configureRouting(
        component.characterController,
        component.wsController,
    )

    component.playerTracker.start()
}

private fun Application.configureSerialization(json: Json) {
    install(WebSockets)
    install(ContentNegotiation) {
        json(json)
    }
}

fun Application.configureRouting(
    characterController: CharacterController,
    wsController: WSController,
) {
    routing {
        webSocket("/ws") {
            wsController.handleWSConnection(this)
        }
        get("/character") {
            val rawCharacterId = call.request.queryParameters["character_id"]
            val rawNamespace = call.request.queryParameters["namespace"]

            if (rawCharacterId.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val namespace = rawNamespace?.toNamespace()
            if (namespace == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val character = characterController.getCharacter(
                characterId = rawCharacterId,
                namespace = namespace,
            )
            if (character == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(character)
            }
        }
    }
}
