package com.cramsan.ps2link.core.models

enum class Namespace {
    PS2PC,
    PS2PS4US,
    PS2PS4EU,
    UNDETERMINED;

    companion object {
        val validNamespaces = listOf(PS2PC, PS2PS4EU, PS2PS4US)
    }
}
