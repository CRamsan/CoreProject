package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    var item_id: String,
    var item_type_id: String? = null,
    var item_category_id: String? = null,
    var is_vehicle_weapon: String? = null,
    val name: Name_Multi? = null,
    val description: Description? = null,
    var faction_id: String? = null,
    var max_stack_size: String? = null,
    var image_set_id: String? = null,
    var image_id: String? = null,
    var image_path: String? = null,
    var is_default_attachment: String? = null,
)
