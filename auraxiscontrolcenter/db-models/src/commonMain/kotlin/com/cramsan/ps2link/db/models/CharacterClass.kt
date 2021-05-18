package com.cramsan.ps2link.db.models

enum class CharacterClass(val id: Long) {
    LIGHT_ASSAULT(0),
    ENGINEER(1),
    MEDIC(2),
    INFILTRATOR(3),
    HEAVY_ASSAULT(4),
    MAX(5),
    UNKNOWN(-1);

    companion object {

        private val enumMapping: Map<Long, CharacterClass> by lazy {
            values().associateBy { it.id }
        }

        fun fromLong(code: Long?): CharacterClass {
            return enumMapping[code] ?: UNKNOWN
        }
    }
}
