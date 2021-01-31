package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi

data class Vehicle(

    var name: Name_Multi? = null,
    var description: Description? = null,
    var cost: String? = null,
    var cost_resource_id: String? = null,
    var image_id: String? = null,
    var image_path: String? = null,
    var image_set_id: String? = null,
    var type_id: String? = null,
    var type_name: String? = null,
    var vehicle_id: String? = null,

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
)
