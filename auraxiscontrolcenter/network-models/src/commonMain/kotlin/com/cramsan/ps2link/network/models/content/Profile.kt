package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: NameMultiLang? = null
)
