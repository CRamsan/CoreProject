package com.cramsan.ps2link.network.ws.messages

import com.cramsan.framework.test.TestBase
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonExtensionsTests : TestBase() {

    lateinit var json: Json

    override fun setupTest() {
        json = Json {
            classDiscriminator = "_type"
        }
    }

    @Test
    fun testDeserializingConnectedMessage() {
        assertEquals(SERVER_EVENT_CONNECTED, json.parseServerEvent(MESSAGE_CONNECTED))
    }

    @Test
    fun testDeserializingHeartbeat() {
        assertEquals(SERVER_MESSAGE_HEARTBEAT, json.parseServerEvent(MESSAGE_HEARTBEAT))
    }

    @Test
    fun testDeserializingStateChangedMessage() {
        assertEquals(SERVER_EVENT_STATE_CHANGE, json.parseServerEvent(MESSAGE_STATE_CHANGE))
    }

    @Test
    fun testDeserializingServiceMessage() {
        assertEquals(SERVER_EVENT_SERVICE_MESSAGE, json.parseServerEvent(SERVICE_MESSAGE))
    }

    companion object {
        private val MESSAGE_CONNECTED = "{\"connected\":\"true\",\"service\":\"push\",\"type\":\"connectionStateChanged\"}"
        private val SERVER_EVENT_CONNECTED = ConnectionStateChanged(
            connected = "true",
            service = ServiceType.PUSH,
            type = ServerMessageType.CONNECTION_STATE_CHANGED,
        )

        private val MESSAGE_HEARTBEAT = "{\"online\":{\"EventServerEndpoint_Briggs_25\":\"false\",\"EventServerEndpoint_Cobalt_13\":\"true\"},\"service\":\"event\",\"type\":\"heartbeat\"}"
        private val SERVER_MESSAGE_HEARTBEAT = Heartbeat(
            online = mapOf(
                "EventServerEndpoint_Briggs_25" to "false",
                "EventServerEndpoint_Cobalt_13" to "true",
            ),
            service = ServiceType.EVENT,
            type = ServerMessageType.HEARTBEAT,
        )

        private val MESSAGE_STATE_CHANGE = "{\"detail\":\"EventServerEndpoint_Cobalt_13\",\"online\":\"true\",\"service\":\"event\",\"type\":\"serviceStateChanged\"}"
        private val SERVER_EVENT_STATE_CHANGE = ServiceStateChanged(
            detail = "EventServerEndpoint_Cobalt_13",
            online = "true",
            service = ServiceType.EVENT,
            type = ServerMessageType.SERVICE_STATE_CHANGED,
        )

        private val SERVICE_MESSAGE = "{\"payload\":{\"attacker_character_id\":\"5428920856031814129\",\"attacker_fire_mode_id\":\"7429\",\"attacker_loadout_id\":\"10\",\"attacker_vehicle_id\":\"0\",\"attacker_weapon_id\":\"7194\",\"character_id\":\"5429026007682689297\",\"character_loadout_id\":\"6\",\"event_name\":\"Death\",\"is_headshot\":\"1\",\"timestamp\":\"1645982556\",\"world_id\":\"40\",\"zone_id\":\"4\"},\"service\":\"event\",\"type\":\"serviceMessage\"}"
        private val SERVER_EVENT_SERVICE_MESSAGE = ServiceMessage(
            service = ServiceType.EVENT,
            type = ServerMessageType.SERVICE_MESSAGE,
            payload = Death(
                attackerCharacterId = "5428920856031814129",
                attackerFireModeId = "7429",
                attackerLoadoutId = "10",
                attackerVehicleId = "0",
                attackerWeaponId = "7194",
                characterId = "5429026007682689297",
                characterLoadoutId = "6",
                eventName = EventName.DEATH,
                isCritical = null,
                isHeadshot = "1",
                timestamp = "1645982556",
                vehicleId = null,
                worldId = "40",
                zoneId = "4",
            )
        )
    }
}
