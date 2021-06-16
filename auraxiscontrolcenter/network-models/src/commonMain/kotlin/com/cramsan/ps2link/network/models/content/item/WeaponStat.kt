package com.cramsan.ps2link.network.models.content.item

import kotlinx.serialization.Serializable

@Serializable
data class WeaponStat(
    val character_id: String? = null,
    val item_id: String? = null,
    val last_save: String? = null,
    val last_save_date: String? = null,
    val stat_name: String? = null,
    val item_id_join_item: WeaponInfo? = null,
    val vehicle_id_join_vehicle: WeaponInfo? = null,
    val value_nc: Int? = 0,
    val value_tr: Int? = 0,
    val value_vs: Int? = 0,
)
