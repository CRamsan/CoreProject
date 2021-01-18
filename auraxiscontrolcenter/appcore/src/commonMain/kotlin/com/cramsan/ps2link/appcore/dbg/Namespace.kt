package com.cramsan.ps2link.appcore.dbg

enum class Namespace constructor(private val namespace: String) {
    PS2PC("ps2:v2"),
    PS2PS4US("ps2ps4us:v2"),
    PS2PS4EU("ps2ps4eu:v2");

    override fun toString(): String {
        return this.namespace
    }
}
