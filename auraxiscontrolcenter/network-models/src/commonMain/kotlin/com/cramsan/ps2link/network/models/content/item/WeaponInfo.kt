package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class WeaponInfo(
    val name: Name_Multi? = null,
    val image_path: String? = null,
)
