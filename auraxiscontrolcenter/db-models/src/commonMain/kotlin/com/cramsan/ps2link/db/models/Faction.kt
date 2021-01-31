package com.cramsan.ps2link.db.models

enum class Faction(val code: String) {
    VS("1"),
    NC("2"),
    TR("3"),
    NS("4"),
    UNKNOWN(""), ;

    companion object {

        private val enumMapping: Map<String, Faction> by lazy {
            values().associateBy { it.code }
        }

        fun fromString(code: String?): Faction {
            return enumMapping[code] ?: UNKNOWN
        }
    }
}
