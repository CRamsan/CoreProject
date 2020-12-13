package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi

data class Item(
    var name: Name_Multi? = null,
    var description: Description? = null,

    var faction_id: String? = null,
    var image_id: String? = null,
    var image_path: String? = null,
    var image_set_id: String? = null,
    var is_default_attachment: String? = null,
    var is_vehicle_weapon: String? = null,
    var item_category_id: String? = null,
    var item_id: String? = null,
    var item_type_id: String? = null,
    var max_stack_size: String? = null,
)
/*
    override val nameText: String?
        get() = if (name == null) {
            null
        } else {
            name!!.en
        }

    override val imagePath: String?
        get() = image_path
 */
