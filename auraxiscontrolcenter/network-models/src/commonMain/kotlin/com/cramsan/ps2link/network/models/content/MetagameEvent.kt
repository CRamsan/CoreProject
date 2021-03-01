package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.Serializable

/**
 * Created by cramsan on 9/13/15.
 */
@Serializable
data class MetagameEvent(
    var metagame_event_id: String? = null,
    var type: String? = null,
    var experience_bonus: String? = null,
    var name: Name_Multi? = null,
    var description: Name_Multi? = null,
)
