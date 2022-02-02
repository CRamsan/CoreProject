package com.cramsan.ps2link.network.models.content.item

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

@Serializable
data class WeaponInfo(
    val name: NameMultiLang? = null,
    val image_path: String? = null,
)
