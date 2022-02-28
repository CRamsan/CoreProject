package com.cramsan.ps2link.network.ws.messages

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

/**
 * Serialize an [event] to a String.
 */
fun Json.createSerializedMessage(event: ServerEvent) = encodeToString(event)

/**
 * Serialize an [event] to a String.
 */
fun Json.createSerializedClientMessage(event: ClientCommand) = encodeToString(event)

/**
 * Deserialize a [textFrame] to a [ServerEvent].
 */
fun Json.parseServerEvent(textFrame: String): ServerEvent {
    val root = parseToJsonElement(textFrame).jsonObject
    val typeString = root.getValue("type").toString()
    val type: ServerMessageType = decodeFromString(typeString)
    return when (type) {
        ServerMessageType.CONNECTION_STATE_CHANGED -> {
            val result: ConnectionStateChanged = decodeFromString(textFrame)
            result
        }
        ServerMessageType.HEARTBEAT -> {
            val result: Heartbeat = decodeFromString(textFrame)
            result
        }
        ServerMessageType.SERVICE_STATE_CHANGED -> {
            val result: ServiceStateChanged = decodeFromString(textFrame)
            result
        }
        ServerMessageType.SERVICE_MESSAGE -> {
            parseServerEventPayload(textFrame)
        }
    }
}

private fun Json.parseServerEventPayload(textFrame: String): ServerEvent {
    val root = parseToJsonElement(textFrame).jsonObject
    val eventNameString = root.getValue("payload").jsonObject.getValue("event_name").toString()
    val eventName: EventName = decodeFromString(eventNameString)

    return when (eventName) {
        EventName.CONTINENT_LOCK -> {
            val result: ServiceMessage<ContinentLock> = decodeFromString(textFrame)
            result
        }
        EventName.CONTINENT_UNLOCK -> {
            val result: ServiceMessage<ContinentUnlock> = decodeFromString(textFrame)
            result
        }
        EventName.FACILITY_CONTROL -> {
            val result: ServiceMessage<FacilityControl> = decodeFromString(textFrame)
            result
        }
        EventName.METAGAME_EVENT -> {
            val result: ServiceMessage<MetagameEvent> = decodeFromString(textFrame)
            result
        }
        EventName.ACHIEVEMENT_EARNED -> {
            val result: ServiceMessage<AchievementEarned> = decodeFromString(textFrame)
            result
        }
        EventName.BATTLE_RANK_UP -> {
            val result: ServiceMessage<BattleRankUp> = decodeFromString(textFrame)
            result
        }
        EventName.DEATH -> {
            val result: ServiceMessage<Death> = decodeFromString(textFrame)
            result
        }
        EventName.GAIN_EXPERIENCE -> {
            val result: ServiceMessage<GainExperience> = decodeFromString(textFrame)
            result
        }
        EventName.ITEM_ADDED -> {
            val result: ServiceMessage<ItemAdded> = decodeFromString(textFrame)
            result
        }
        EventName.PLAYER_FACILITY_CAPTURE -> {
            val result: ServiceMessage<PlayerFacilityCapture> = decodeFromString(textFrame)
            result
        }
        EventName.PLAYER_FACILITY_DEFEND -> {
            val result: ServiceMessage<PlayerFacilityDefend> = decodeFromString(textFrame)
            result
        }
        EventName.PLAYER_LOGIN -> {
            val result: ServiceMessage<PlayerLogin> = decodeFromString(textFrame)
            result
        }
        EventName.PLAYER_LOGOUT -> {
            val result: ServiceMessage<PlayerLogout> = decodeFromString(textFrame)
            result
        }
        EventName.SKILL_ADDED -> {
            val result: ServiceMessage<SkillAdded> = decodeFromString(textFrame)
            result
        }
        EventName.VEHICLE_DESTROY -> {
            val result: ServiceMessage<VehicleDestroy> = decodeFromString(textFrame)
            result
        }
    }
}
