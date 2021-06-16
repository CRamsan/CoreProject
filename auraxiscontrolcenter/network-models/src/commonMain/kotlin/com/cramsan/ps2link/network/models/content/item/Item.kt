package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val item_id: String,
    val item_type_id: String? = null,
    val item_category_id: String? = null,
    val is_vehicle_weapon: String? = null,
    val name: Name_Multi? = null,
    val description: Description? = null,
    val faction_id: String? = null,
    val max_stack_size: String? = null,
    val image_set_id: String? = null,
    val image_id: String? = null,
    val image_path: String? = null,
    val is_default_attachment: String? = null,
)
