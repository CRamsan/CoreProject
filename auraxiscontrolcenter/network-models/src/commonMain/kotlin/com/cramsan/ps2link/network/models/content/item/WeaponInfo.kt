package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

@Serializable
data class WeaponInfo(
    var name: Name_Multi? = null,
    var image_path: String? = null,
)
