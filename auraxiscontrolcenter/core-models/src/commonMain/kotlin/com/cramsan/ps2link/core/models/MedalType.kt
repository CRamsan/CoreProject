package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/26/2021
 */
enum class MedalType(val goal: Int) {
    AURAXIUM(1160),
    GOLD(160),
    SILVER(60),
    BRONCE(10),
    NONE(0),
}

fun Long.toMedalType() = when (this) {
    in MedalType.AURAXIUM.goal..Int.MAX_VALUE -> MedalType.AURAXIUM
    in MedalType.GOLD.goal until MedalType.AURAXIUM.goal -> MedalType.GOLD
    in MedalType.SILVER.goal until MedalType.GOLD.goal -> MedalType.SILVER
    in MedalType.BRONCE.goal until MedalType.SILVER.goal -> MedalType.BRONCE
    else -> MedalType.NONE
}
