package com.cesarandres.ps2link.fragments.profilepager.killlist

import android.net.Uri
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.ui.Faction
import com.cramsan.ps2link.ui.KillType

/**
 * @Author cramsan
 * @created 1/26/2021
 */
data class KillEvent(
    val characterId: String?,
    val namespace: Namespace,
    val killType: KillType = KillType.UNKNOWN,
    val faction: Faction = Faction.UNKNOWN,
    val attacker: String? = null,
    val time: Long? = null,
    val weaponName: String? = null,
    val weaponImage: Uri = Uri.EMPTY,
)
