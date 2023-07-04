package com.cramsan.ps2link.network.ws.testgui.ui.screens.tracker

import com.cramsan.ps2link.core.models.CharacterClass
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.KillType
import com.cramsan.ps2link.core.models.Namespace

sealed class PlayerEventUIModel

data class PlayerKillUIModel(
    val killType: KillType,
    val profile: CharacterClass?,
    val faction: Faction,
    val playerName: String?,
    val playerRank: Int?,
    val characterId: String?,
    val namespace: Namespace,
    val time: String?,
    val weaponName: String?,
    val weaponImage: String?,
) : PlayerEventUIModel()
