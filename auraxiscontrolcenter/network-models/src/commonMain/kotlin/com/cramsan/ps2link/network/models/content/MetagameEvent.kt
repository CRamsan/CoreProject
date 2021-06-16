package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

/**
 * Created by cramsan on 9/13/15.
 */
@Serializable
data class MetagameEvent(
    val metagame_event_id: String? = null,
    val type: String? = null,
    val experience_bonus: String? = null,
    val name: Name_Multi? = null,
    val description: Name_Multi? = null,
)
