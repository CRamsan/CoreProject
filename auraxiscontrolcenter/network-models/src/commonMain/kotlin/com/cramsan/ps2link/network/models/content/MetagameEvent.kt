package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.NameMultiLang
import kotlinx.serialization.Serializable

/**
 * Created by cramsan on 9/13/15.
 */
@Serializable
data class MetagameEvent(
    val metagame_event_id: String? = null,
    val type: String? = null,
    val experience_bonus: String? = null,
    val name: NameMultiLang? = null,
    val description: NameMultiLang? = null,
)
