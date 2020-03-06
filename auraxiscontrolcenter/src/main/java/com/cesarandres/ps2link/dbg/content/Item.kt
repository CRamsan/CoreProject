package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.item.IContainDrawable
import com.cesarandres.ps2link.dbg.content.world.Name_Multi

class Item : IContainDrawable {

    var name: Name_Multi? = null
    var description: Description? = null

    var faction_id: String? = null
    var image_id: String? = null
    var image_path: String? = null
    var image_set_id: String? = null
    var is_default_attachment: String? = null
    var is_vehicle_weapon: String? = null
    var item_category_id: String? = null
    var item_id: String? = null
    var item_type_id: String? = null
    var max_stack_size: String? = null

    override val nameText: String?
        get() = if (name == null) {
            null
        } else {
            name!!.localizedName
        }

    override val imagePath: String?
        get() = image_path
}
