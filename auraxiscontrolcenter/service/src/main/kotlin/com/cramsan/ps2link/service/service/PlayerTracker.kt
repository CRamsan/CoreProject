package com.cramsan.ps2link.service.service

import com.cramsan.ps2link.network.ws.StreamingClient
import com.cramsan.ps2link.network.ws.StreamingClientEventHandler
import com.cramsan.ps2link.network.ws.messages.AchievementEarned
import com.cramsan.ps2link.network.ws.messages.BattleRankUp
import com.cramsan.ps2link.network.ws.messages.ConnectionStateChanged
import com.cramsan.ps2link.network.ws.messages.ContinentLock
import com.cramsan.ps2link.network.ws.messages.ContinentUnlock
import com.cramsan.ps2link.network.ws.messages.Death
import com.cramsan.ps2link.network.ws.messages.EventType
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
import com.cramsan.ps2link.network.ws.messages.ServiceMessage
import com.cramsan.ps2link.network.ws.messages.ServiceStateChanged
import com.cramsan.ps2link.network.ws.messages.SkillAdded
import com.cramsan.ps2link.network.ws.messages.SubscriptionConfirmation
import com.cramsan.ps2link.network.ws.messages.UnhandledEvent
import com.cramsan.ps2link.network.ws.messages.VehicleDestroy
import com.cramsan.ps2link.network.ws.messages.WorldSubscribe
import com.cramsan.ps2link.service.api.models.Namespace
import com.cramsan.ps2link.service.controller.census.DBGServiceClient
import com.cramsan.ps2link.service.controller.domain.CharacterController
import com.cramsan.ps2link.service.toCensusModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PlayerTracker(
    private val dbgServiceClient: DBGServiceClient,
    private val streamingClient: StreamingClient,
    private val coroutineScope: CoroutineScope,
    private val characterController: CharacterController,
) {

    private val eventHandler = object : StreamingClientEventHandler {
        override fun onServerEventReceived(serverEvent: ServerEvent) {
            when (serverEvent) {
                is ConnectionStateChanged -> Unit
                is Heartbeat -> Unit
                is ServiceMessage<*> -> { handleServerEventPayload(serverEvent.payload) }
                is ServiceStateChanged -> Unit
                is SubscriptionConfirmation -> Unit
                is UnhandledEvent -> Unit
            }
        }
    }

    fun start() {
        streamingClient.registerListener(eventHandler)
        streamingClient.start()
        coroutineScope.launch {
            subscribeToPlayerLogin()
        }
    }

    private suspend fun subscribeToPlayerLogin() {
        val servers = dbgServiceClient.getWorlds(Namespace.PS2PC.toCensusModel())

        val serverIds = servers?.world_list?.mapNotNull { it.world_id } ?: return

        streamingClient.sendMessage(
            WorldSubscribe(
                worlds = serverIds,
                eventNames = listOf(
                    EventType.PLAYER_LOGIN,
                ),
            ),
        )
    }

    private fun handleServerEventPayload(payload: ServerEventPayload?) {
        when (payload) {
            is AchievementEarned -> Unit
            is BattleRankUp -> Unit
            is ContinentLock -> Unit
            is ContinentUnlock -> Unit
            is Death -> Unit
            is FacilityControl -> Unit
            is GainExperience -> Unit
            is ItemAdded -> Unit
            is MetagameEvent -> Unit
            is PlayerFacilityCapture -> Unit
            is PlayerFacilityDefend -> Unit
            is PlayerLogin -> {
                triggerPlayerCache(payload)
            }
            is PlayerLogout -> Unit
            is SkillAdded -> Unit
            is VehicleDestroy -> Unit
            null -> Unit
            is ServerEventPayloadV2.DeathV2 -> Unit
        }
    }

    private fun triggerPlayerCache(payload: PlayerLogin) {
        coroutineScope.launch {
            val characterId = payload.characterId ?: return@launch
            characterController.getCharacter(characterId, namespace = Namespace.PS2PC)
        }
    }
}
