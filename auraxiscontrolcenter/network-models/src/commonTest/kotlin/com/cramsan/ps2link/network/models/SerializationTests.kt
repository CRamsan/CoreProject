package com.cramsan.ps2link.network.models

import com.cramsan.ps2link.network.models.content.OnlineStatus
import com.cramsan.ps2link.network.models.content.response.Character_name_list_response
import com.cramsan.ps2link.network.models.content.response.Server_Status_response
import com.cramsan.ps2link.network.models.content.response.Server_response
import com.cramsan.ps2link.network.models.content.response.server.PopulationStatus
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SerializationTests {

    lateinit var json: Json

    @BeforeTest
    fun setUp() {
        json = Json {
            ignoreUnknownKeys = true
        }
    }

    /**
     * Test getting all the server's population. This is not a regular Census API call so use it with care.
     * https://census.daybreakgames.com/s:PS2Link/json/status/ps2
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testServerPopulationJson() {
        val response = "{\"ps2\":{\"Live\":{\"Cobalt (EU)\":{\"region_code\":\"live\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":62,\"age\":\"00:01:02\"},\"Connery (US West)\":{\"region_code\":\"live\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":97,\"age\":\"00:01:37\"},\"Emerald (US East)\":{\"region_code\":\"live\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":92,\"age\":\"00:01:32\"},\"Miller (EU)\":{\"region_code\":\"live\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":97,\"age\":\"00:01:37\"},\"SolTech (Asia)\":{\"region_code\":\"live\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":67,\"age\":\"00:01:07\"}},\"Live PS4\":{\"Ceres (EU)\":{\"region_code\":\"live_ps4\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":94,\"age\":\"00:01:34\"},\"Genudine\":{\"region_code\":\"live_ps4\",\"title\":\"PlanetSide 2\",\"status\":\"low\",\"ageSeconds\":61,\"age\":\"00:01:01\"}}}}"
        val parsedResponse = json.decodeFromString<Server_Status_response>(response)
        assertEquals("00:01:34", parsedResponse.ps2.livePS4?.ceres?.age)
        assertEquals(PopulationStatus.LOW, parsedResponse.ps2.livePS4?.ceres?.status)
        assertEquals(94, parsedResponse.ps2.livePS4?.ceres?.ageSeconds)
        assertNotNull(parsedResponse.ps2.livePS4?.genudine)
        assertNotNull(parsedResponse.ps2.live?.cobalt)
        assertNotNull(parsedResponse.ps2.live?.connery)
        assertNotNull(parsedResponse.ps2.live?.emerald)
        assertNotNull(parsedResponse.ps2.live?.miller)
    }

    /**
     * Test getting all servers within a namespace.
     * https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/world/?c%3AlimitPerDB=20&c%3Alang=en
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testServerStatusJson() {
        val response = "{\"world_list\":[{\"world_id\":\"1\",\"state\":\"online\",\"name\":{\"en\":\"Connery\"}},{\"world_id\":\"13\",\"state\":\"online\",\"name\":{\"en\":\"Cobalt\"}},{\"world_id\":\"10\",\"state\":\"online\",\"name\":{\"en\":\"Miller\"}},{\"world_id\":\"17\",\"state\":\"online\",\"name\":{\"en\":\"Emerald\"}},{\"world_id\":\"40\",\"state\":\"online\",\"name\":{\"en\":\"SolTech\"}},{\"world_id\":\"19\",\"state\":\"online\",\"name\":{\"en\":\"Jaeger\"}},{\"world_id\":\"24\",\"state\":\"locked\",\"name\":{\"en\":\"Apex\"}},{\"world_id\":\"25\",\"state\":\"locked\",\"name\":{\"en\":\"Briggs\"},\"description\":{\"en\":\"AUS\"}}],\"returned\":8}"
        val parsedResponse = json.decodeFromString<Server_response>(response)
        assertEquals(8, parsedResponse.world_list.size)
        val server = parsedResponse.world_list.first()
        assertEquals("1", server.world_id)
        assertEquals(OnlineStatus.ONLINE, server.state)
        assertEquals("Connery", server.name?.en)
    }

    /**
     * Test getting a list of characters matching a name.
     * https://census.daybreakgames.com/s:PS2Link/get/ps2ps4us:v2/character_name/?name.first_lower=%5Ecramsan&c%3Alimit=25&c%3Ajoin=character&c%3Alang=en
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testSearchingCharacterUSJson() {
        val response = "{\"character_name_list\":[{\"character_id\":\"5428356399611664305\",\"name\":{\"first\":\"CRamsan\",\"first_lower\":\"cramsan\"},\"character_id_join_character\":{\"character_id\":\"5428356399611664305\",\"name\":{\"first\":\"CRamsan\",\"first_lower\":\"cramsan\"},\"faction_id\":\"1\",\"head_id\":\"4\",\"title_id\":\"37\",\"times\":{\"creation\":\"1436243249\",\"creation_date\":\"2015-07-07 04:27:29.0\",\"last_save\":\"1584167881\",\"last_save_date\":\"2020-03-14 06:38:01.0\",\"last_login\":\"1584165426\",\"last_login_date\":\"2020-03-14 05:57:06.0\",\"login_count\":\"87\",\"minutes_played\":\"3054\"},\"certs\":{\"earned_points\":\"2167\",\"gifted_points\":\"1532\",\"spent_points\":\"3353\",\"available_points\":\"346\",\"percent_to_next\":\"0.34\"},\"battle_rank\":{\"percent_to_next\":\"76\",\"value\":\"26\"},\"profile_id\":\"19\",\"daily_ribbon\":{\"count\":\"0\",\"time\":\"1584144000\",\"date\":\"2020-03-14 00:00:00.0\"}}},{\"character_id\":\"5428387482490675569\",\"name\":{\"first\":\"CRamsanTR\",\"first_lower\":\"cramsantr\"},\"character_id_join_character\":{\"character_id\":\"5428387482490675569\",\"name\":{\"first\":\"CRamsanTR\",\"first_lower\":\"cramsantr\"},\"faction_id\":\"2\",\"head_id\":\"1\",\"title_id\":\"0\",\"times\":{\"creation\":\"1443917728\",\"creation_date\":\"2015-10-04 00:15:28.0\",\"last_save\":\"1443920148\",\"last_save_date\":\"2015-10-04 00:55:48.0\",\"last_login\":\"1443918387\",\"last_login_date\":\"2015-10-04 00:26:27.0\",\"login_count\":\"2\",\"minutes_played\":\"29\"},\"certs\":{\"earned_points\":\"14\",\"gifted_points\":\"210\",\"spent_points\":\"105\",\"available_points\":\"119\",\"percent_to_next\":\"0.72\"},\"battle_rank\":{\"percent_to_next\":\"96\",\"value\":\"3\"},\"profile_id\":\"7\",\"daily_ribbon\":{\"count\":\"0\",\"time\":\"1443855600\",\"date\":\"2015-10-03 07:00:00.0\"}}}],\"returned\":2}"
        val parsedResponse = json.decodeFromString<Character_name_list_response>(response)
        assertEquals(2, parsedResponse.character_name_list.size)
        val baseCharacter = parsedResponse.character_name_list[0]
        val character = baseCharacter.character_id_join_character
        assertEquals("5428356399611664305", baseCharacter.character_id)
        assertEquals("5428356399611664305", character.character_id)
        assertEquals("CRamsan", baseCharacter.name?.first)
        assertEquals("cramsan", baseCharacter.name?.first_lower)
        assertEquals("CRamsan", character.name?.first)
        assertEquals("cramsan", character.name?.first_lower)
        assertEquals("1", character.faction_id)
        assertEquals("1584165426", character.times?.last_login)
        assertEquals("3054", character.times?.minutes_played)
        assertEquals("346", character.certs?.available_points)
        assertEquals("0.34", character.certs?.percent_to_next)
        assertEquals(26, character.battle_rank?.value)
        assertEquals(76, character.battle_rank?.percent_to_next)
        assertEquals("19", character.profile_id)
    }

    /**

     https://www.reddit.com/r/Planetside/hot.json

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character_name/?name.first_lower=%5Ecramsan&c%3Alimit=25&c%3Ajoin=character&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2ps4eu:v2/character_name/?name.first_lower=%5Ecramsan&c%3Alimit=25&c%3Ajoin=character&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/characters_friend/?character_id=5428010618041058369&c%3Aresolve=character_name&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=stat_history&c%3Ahide=name%2Cbattle_rank%2Ccerts%2Ctimes%2Cdaily_ribbon&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/characters_event/?character_id=5428010618041058369&c%3Aresolve=character%2Cattacker&c%3Alimit=100&type=DEATH%2CKILL&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/item/?item_id=6009069&item_id=79&item_id=17000&item_id=17012&item_id=7533&item_id=16000&item_id=0&item_id=17016&item_id=26001&item_id=6005713&item_id=4906&item_id=7494&item_id=6005331&item_id=6004216&item_id=44&item_id=7276&item_id=13&item_id=15012&item_id=43&item_id=7190&item_id=7400&item_id=3442&item_id=28000&item_id=50560&item_id=7256&item_id=3158&item_id=802106&item_id=6006332&item_id=6555&item_id=3103&item_id=3102&item_id=432&item_id=7126&item_id=75083&item_id=7170&item_id=128&item_id=7174&item_id=44505&item_id=7104&item_id=3107&item_id=4305&item_id=40001&item_id=19&item_id=3701&item_id=78&item_id=6004753&item_id=7173&item_id=75063&item_id=44705&item_id=1879&item_id=4&item_id=6119&item_id=24001&item_id=6005453&item_id=802299&item_id=1969&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/vehicle/?vehicle_id=0&vehicle_id=11&vehicle_id=9&vehicle_id=4&vehicle_id=10&vehicle_id=3&vehicle_id=12&vehicle_id=14&vehicle_id=7&vehicle_id=5&vehicle_id=1&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/characters_weapon_stat_by_faction/?character_id=5428010618041058369&c%3Ajoin=item%5Eshow%3Aimage_path%27name.en&c%3Ajoin=vehicle%5Eshow%3Aimage_path%27name.en&c%3Alimit=10000&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/outfit/37509488620628689?c%3Aresolve=leader&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/outfit_member?c%3Alimit=10000&c%3Aresolve=online_status%2Ccharacter%28name%2Cbattle_rank%2Cprofile_id%29&c%3Ajoin=type%3Aprofile%5Elist%3A0%5Einject_at%3Aprofile%5Eshow%3Aname.en%5Eon%3Acharacter.profile_id%5Eto%3Aprofile_id&outfit_id=37509488620628689&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/outfit/?outfit_id=37509488620628689&c%3Aresolve=member_online_status%2Cmember%2Cmember_character%28name%2Ctype.faction%29&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/outfit/37509488620628689?c%3Aresolve=leader&c%3Alang=en

     https://census.daybreakgames.com/s:PS2Link/get/ps2:v2/character/5428010618041058369?c%3Aresolve=outfit%2Cworld%2Conline_status&c%3Ajoin=type%3Aworld%5Einject_at%3Aserver&c%3Alang=en

     */
}
