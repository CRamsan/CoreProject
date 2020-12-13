package com.cramsan.ps2link.appcore.dbg.content.item

data class Weapon(
    var character_id: String? = null,
    var item_id: String? = null,
    var last_save: String? = null,
    var last_save_date: String? = null,
    var stat_name: String? = null,
    var item_id_join_item: WeaponInfo? = null,
    var vehicle_id_join_vehicle: WeaponInfo? = null,
    var value_nc: Int = 0,
    var value_tr: Int = 0,
    var value_vs: Int = 0,
)
