package com.cramsan.ps2link.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.MedalType
import com.cramsan.ps2link.core.models.Namespace

@Composable
internal actual fun CharacterClass.toPainter(): Painter {
    val resourcePath = when (this) {
        CharacterClass.LIGHT_ASSAULT -> "icon_lia.webp"
        CharacterClass.ENGINEER -> "icon_eng.webp"
        CharacterClass.MEDIC -> "icon_med.webp"
        CharacterClass.INFILTRATOR -> "icon_inf.webp"
        CharacterClass.HEAVY_ASSAULT -> "icon_hea.webp"
        CharacterClass.MAX -> "icon_max.webp"
        CharacterClass.UNKNOWN -> "icon_lia.webp"
    }
    return painterResource(resourcePath)
}

@Composable
internal actual fun MedalType?.toPainter(): Painter {
    val resourcePath = when (this) {
        MedalType.AURAXIUM -> "medal_araxium.webp"
        MedalType.GOLD -> "medal_gold.webp"
        MedalType.SILVER -> "medal_silver.webp"
        MedalType.BRONCE -> "medal_copper.webp"
        MedalType.NONE, null -> "medal_empty.webp"
    }
    return painterResource(resourcePath)
}

@Composable
internal actual fun Faction.toPainter(): Painter {
    val resourcePath = when (this) {
        Faction.VS -> "icon_faction_vs.webp"
        Faction.NC -> "icon_faction_nc.webp"
        Faction.TR -> "icon_faction_tr.webp"
        Faction.NS -> "icon_faction_ns.webp"
        Faction.UNKNOWN -> "icon_faction_ns.webp"
    }
    return painterResource(resourcePath)
}

@Composable
internal actual fun Namespace.toPainter(): Painter {
    val resourcePath = when (this) {
        Namespace.PS2PC -> "namespace_pc.webp"
        Namespace.PS2PS4US -> "namespace_ps4us.webp"
        Namespace.PS2PS4EU -> "namespace_ps4eu.webp"
        else -> {
            "namespace_pc.webp"
        }
    }
    return painterResource(resourcePath)
}