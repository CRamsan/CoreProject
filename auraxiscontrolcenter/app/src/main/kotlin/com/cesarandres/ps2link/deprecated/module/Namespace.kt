package com.cesarandres.ps2link.deprecated.module

enum class Namespace(private val namespace: String) {
    PS2PC("ps2:v2"),
    PS2PS4US("ps2ps4us:v2"),
    PS2PS4EU("ps2ps4eu:v2");

    override fun toString(): String {
        return this.namespace
    }
}

fun Namespace.toCoreModel(): com.cramsan.ps2link.core.models.Namespace {
    return when (this) {
        Namespace.PS2PC -> com.cramsan.ps2link.core.models.Namespace.PS2PC
        Namespace.PS2PS4US -> com.cramsan.ps2link.core.models.Namespace.PS2PS4US
        Namespace.PS2PS4EU -> com.cramsan.ps2link.core.models.Namespace.PS2PS4EU
    }
}
