package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/16/2021
 */
enum class Faction {
    VS,
    NC,
    TR,
    NS,
    UNKNOWN,
    ;
    companion object {
        private val enumMapping: Map<String, Faction> by lazy {
            values().associateBy { it.name }
        }

        /**
         * Convert [faction] strings to an instance of [Faction].
         */
        fun fromString(faction: String?): Faction {
            return enumMapping[faction] ?: UNKNOWN
        }
    }
}
