package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val vehicle_id: String,
    val name: NameMultiLang? = null,
    val description: Description? = null,
    val type_id: String? = null,
    val type_name: String? = null,
    val cost: String? = null,
    val cost_resource_id: String? = null,
    val image_set_id: String? = null,
    val image_id: String? = null,
    val image_path: String? = null,
)
