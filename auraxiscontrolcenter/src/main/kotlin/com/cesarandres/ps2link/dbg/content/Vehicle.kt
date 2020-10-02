package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.item.IContainDrawable
import com.cesarandres.ps2link.dbg.content.world.Name_Multi

class Vehicle : IContainDrawable {

    var name: Name_Multi? = null
    var description: Description? = null
    var cost: String? = null
    var cost_resource_id: String? = null
    var image_id: String? = null
    var image_path: String? = null
    var image_set_id: String? = null
    var type_id: String? = null
    var type_name: String? = null
    var vehicle_id: String? = null

    override val nameText: String?
        get() = if (name == null) {
            null
        } else {
            name!!.en
        }

    override val imagePath: String?
        get() = image_path
}
