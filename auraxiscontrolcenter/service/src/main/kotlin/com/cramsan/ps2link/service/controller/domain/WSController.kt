package com.cramsan.ps2link.service.controller.domain

import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.StreamingClientEventHandler
import com.cramsan.ps2link.network.ws.messages.AchievementEarned
import com.cramsan.ps2link.network.ws.messages.BattleRankUp
import com.cramsan.ps2link.network.ws.messages.ConnectionStateChanged
import com.cramsan.ps2link.network.ws.messages.ContinentLock
import com.cramsan.ps2link.network.ws.messages.ContinentUnlock
import com.cramsan.ps2link.network.ws.messages.Death
import com.cramsan.ps2link.network.ws.messages.EventName
import com.cramsan.ps2link.network.ws.messages.FacilityControl
import com.cramsan.ps2link.network.ws.messages.GainExperience
import com.cramsan.ps2link.network.ws.messages.Heartbeat
import com.cramsan.ps2link.network.ws.messages.ItemAdded
import com.cramsan.ps2link.network.ws.messages.MetagameEvent
import com.cramsan.ps2link.network.ws.messages.PlayerFacilityCapture
import com.cramsan.ps2link.network.ws.messages.PlayerFacilityDefend
import com.cramsan.ps2link.network.ws.messages.PlayerLogin
import com.cramsan.ps2link.network.ws.messages.PlayerLogout
import com.cramsan.ps2link.network.ws.messages.ServerEvent
import com.cramsan.ps2link.network.ws.messages.ServerEventPayload
import com.cramsan.ps2link.network.ws.messages.ServerEventPayloadV2
import com.cramsan.ps2link.network.ws.messages.ServerMessageType
import com.cramsan.ps2link.network.ws.messages.ServiceMessage
import com.cramsan.ps2link.network.ws.messages.ServiceStateChanged
import com.cramsan.ps2link.network.ws.messages.ServiceType
import com.cramsan.ps2link.network.ws.messages.SkillAdded
import com.cramsan.ps2link.network.ws.messages.SubscriptionConfirmation
import com.cramsan.ps2link.network.ws.messages.UnhandledEvent
import com.cramsan.ps2link.network.ws.messages.VehicleDestroy
import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.controller.census.DBGCensus.Companion.ENDPOINT_URL
import com.cramsan.ps2link.service.di.WS_CLIENT
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class WSController(
    private val characterController: CharacterController,
    private val itemController: ItemController,
    private val coroutineScope: CoroutineScope,
    private val json: Json,
) {
    suspend fun handleWSConnection(session: DefaultWebSocketServerSession) {
        val component = object : KoinComponent {
            val streamingClient: StreamingClient by inject(named(WS_CLIENT))
        }

        val streamingClient: StreamingClient = component.streamingClient
        streamingClient.start()

        val listener = object : StreamingClientEventHandler {
            override fun onServerEventReceived(serverEvent: ServerEvent) {
                when (serverEvent) {
                    is ConnectionStateChanged -> Unit
                    is Heartbeat -> Unit
                    is ServiceMessage<*> -> {
                        coroutineScope.launch {
                            handleServerEventPayload(
                                session,
                                serverEvent.payload,
                            )
                        }
                    }
                    is ServiceStateChanged -> Unit
                    is SubscriptionConfirmation -> Unit
                    is UnhandledEvent -> Unit
                }
            }
        }

        streamingClient.registerListener(listener)

        try {
            for (frame in session.incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                streamingClient.sendRawMessage(receivedText)
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            streamingClient.deregisterListener(listener)
        }
    }

    private suspend fun handleServerEventPayload(
        session: DefaultWebSocketSession,
        payload: ServerEventPayload?,
    ) {
        val event = when (payload) {
            is AchievementEarned -> null
            is BattleRankUp -> null
            is ContinentLock -> null
            is ContinentUnlock -> null
            is Death -> mapDeathEvent(payload)
            is FacilityControl -> null
            is GainExperience -> null
            is ItemAdded -> null
            is MetagameEvent -> null
            is PlayerFacilityCapture -> null
            is PlayerFacilityDefend -> null
            is PlayerLogin -> null
            is PlayerLogout -> null
            is SkillAdded -> null
            is VehicleDestroy -> null
            is ServerEventPayloadV2.DeathV2 -> null
            null -> null
        } ?: return

        val message = json.encodeToString(event)
        session.send(message)
    }

    private suspend fun mapDeathEvent(payload: Death): ServiceMessage<ServerEventPayloadV2.DeathV2>? {
        val characterId = payload.characterId
        val attackerCharacterId = payload.attackerCharacterId

        var attackerCharacterName: String? = null
        var attackerCharacterFaction: String? = null
        var attackerCharacterRank: Int? = null
        var characterName: String? = null
        var characterFaction: String? = null
        var characterRank: Int? = null

        if (characterId != null) {
            val cachedCharacter = characterController.getCharacter(
                characterId,
                Namespace.PS2PC,
                CacheBehaviour.USE_EXPIRED,
                FetchBehaviour.ASYNC_RETRIEVE,
            )
            characterName = cachedCharacter?.name
            characterRank = cachedCharacter?.battleRank?.value
            characterFaction = cachedCharacter?.faction?.name
        }

        if (characterId != attackerCharacterId && attackerCharacterId != null) {
            val cachedAttackerCharacter = characterController.getCharacter(
                attackerCharacterId,
                Namespace.PS2PC,
                CacheBehaviour.USE_EXPIRED,
                FetchBehaviour.ASYNC_RETRIEVE,
            )
            attackerCharacterName = cachedAttackerCharacter?.name
            attackerCharacterRank = cachedAttackerCharacter?.battleRank?.value
            attackerCharacterFaction = cachedAttackerCharacter?.faction?.name
        }

        var attackerWeaponName: String? = null
        var attackerWeaponImageUrl: String? = null
        payload.attackerWeaponId?.let { weaponId ->
            val cachedItem = itemController.getItem(
                weaponId,
                Namespace.PS2PC,
                CacheBehaviour.USE_EXPIRED,
                FetchBehaviour.ASYNC_RETRIEVE,
            )
            attackerWeaponName = cachedItem?.name
            attackerWeaponImageUrl = cachedItem?.imagePath?.let { "$ENDPOINT_URL/$it" }
        }

        return ServiceMessage(
            service = ServiceType.EVENT,
            type = ServerMessageType.SERVICE_MESSAGE,
            payload = ServerEventPayloadV2.DeathV2(
                attackerCharacterId = payload.attackerCharacterId,
                attackerCharacterName = attackerCharacterName,
                attackerCharacterRank = attackerCharacterRank,
                attackerCharacterFaction = attackerCharacterFaction,
                attackerFireModeId = payload.attackerFireModeId,
                attackerLoadoutId = payload.attackerLoadoutId,
                attackerVehicleId = payload.attackerVehicleId,
                attackerWeaponId = payload.attackerWeaponId,
                characterId = payload.characterId,
                characterName = characterName,
                characterRank = characterRank,
                characterFaction = characterFaction,
                characterLoadoutId = payload.characterLoadoutId,
                eventName = EventName.DEATHV2,
                isCritical = payload.isCritical,
                isHeadshot = payload.isHeadshot,
                timestamp = payload.timestamp,
                vehicleId = payload.vehicleId,
                worldId = payload.worldId,
                zoneId = payload.zoneId,
                attackerWeaponName = attackerWeaponName,
                attackerWeaponImageUrl = attackerWeaponImageUrl,
            )
        )
    }
}
