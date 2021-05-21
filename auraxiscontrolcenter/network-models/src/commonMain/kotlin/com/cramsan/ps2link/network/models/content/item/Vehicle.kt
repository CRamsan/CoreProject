package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    var vehicle_id: String,
    val name: Name_Multi? = null,
    val description: Description? = null,
    var type_id: String? = null,
    var type_name: String? = null,
    var cost: String? = null,
    var cost_resource_id: String? = null,
    var image_set_id: String? = null,
    var image_id: String? = null,
    var image_path: String? = null,
)
