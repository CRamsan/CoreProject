package com.cramsan.ps2link.appcore.dbg

enum class Namespace constructor(val namespace: String) {
    PS2PC("ps2:v2"),
    PS2PS4US("ps2ps4us:v2"),
    PS2PS4EU("ps2ps4eu:v2"),
    UNDETERMINED("unknown");

    override fun toString(): String {
        return this.namespace
    }

    companion object {

        private val enumMapping: Map<String, Namespace> by lazy {
            values().associateBy { it.namespace }
        }

        fun fromString(code: String?): Namespace {
            return enumMapping[code] ?: UNDETERMINED
        }
    }
}
