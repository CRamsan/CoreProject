package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/26/2021
 */
data class WeaponItem(
    val weaponId: String,
    val weaponName: String? = null,
    val vehicleName: String? = null,
    val weaponImage: String? = null,
    val statMapping: Map<WeaponEventType, WeaponStatItem> = hashMapOf(),
    val medalType: MedalType? = MedalType.NONE,
)
