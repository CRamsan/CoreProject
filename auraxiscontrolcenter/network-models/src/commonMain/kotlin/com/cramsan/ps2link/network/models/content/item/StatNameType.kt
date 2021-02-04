package com.cramsan.ps2link.network.models.content.item

/**
 * @Author cramsan
 * @created 2/2/2021
 */
enum class StatNameType(val statName: String) {
    WEAPON_VEHICLE_KILLS("weapon_vehicle_kills"),
    WEAPON_HEADSHOTS("weapon_headshots"),
    WEAPON_KILLED_BY("weapon_killed_by"),
    WEAPON_KILLS("weapon_kills"),
    WEAPON_DAMAGE_TAKEN("weapon_damage_taken_by"),
    WEAPON_DAMAGE_GIVEN("weapon_damage_given");

    companion object {
        private val enumMapping: Map<String, StatNameType> by lazy {
            values().associateBy { it.statName }
        }

        fun fromString(statName: String?): StatNameType? {
            return enumMapping[statName]
        }
    }
}
