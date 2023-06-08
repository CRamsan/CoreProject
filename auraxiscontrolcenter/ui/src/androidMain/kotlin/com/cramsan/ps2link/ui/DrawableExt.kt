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
    val resource = when (this) {
        CharacterClass.LIGHT_ASSAULT -> R.drawable.icon_lia
        CharacterClass.ENGINEER -> R.drawable.icon_eng
        CharacterClass.MEDIC -> R.drawable.icon_med
        CharacterClass.INFILTRATOR -> R.drawable.icon_inf
        CharacterClass.HEAVY_ASSAULT -> R.drawable.icon_hea
        CharacterClass.MAX -> R.drawable.icon_max
        CharacterClass.UNKNOWN -> R.drawable.icon_lia
    }
    return painterResource(resource)
}

@Composable
internal actual fun Namespace.toPainter(): Painter {
    val resourceId = when (this) {
        Namespace.PS2PC -> R.drawable.namespace_pc
        Namespace.PS2PS4US -> R.drawable.namespace_ps4us
        Namespace.PS2PS4EU -> R.drawable.namespace_ps4eu
        else -> {
            R.drawable.namespace_pc
        }
    }
    return painterResource(resourceId)
}

@Composable
internal actual fun Faction.toPainter(): Painter {
    val resourceId = when (this) {
        Faction.VS -> R.drawable.icon_faction_vs
        Faction.NC -> R.drawable.icon_faction_nc
        Faction.TR -> R.drawable.icon_faction_tr
        Faction.NS -> R.drawable.icon_faction_ns
        Faction.UNKNOWN -> R.drawable.icon_faction_ns
    }
    return painterResource(resourceId)
}

@Composable
internal actual fun MedalType?.toPainter(): Painter {
    val resource = when (this) {
        MedalType.AURAXIUM -> R.drawable.medal_araxium
        MedalType.GOLD -> R.drawable.medal_gold
        MedalType.SILVER -> R.drawable.medal_silver
        MedalType.BRONCE -> R.drawable.medal_copper
        MedalType.NONE, null -> R.drawable.medal_empty
    }
    return painterResource(resource)
}
