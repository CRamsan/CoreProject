package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.Serializable

@Serializable
data class WorldEvent(
    val event: EventType? = null,
    val faction_nc: String? = null,
    val faction_tr: String? = null,
    val faction_vs: String? = null,
    val metagame_event_state: String? = null,
    val metagame_event_id: String? = null,
    val timestamp: String? = null,
    val zone_id: String? = null,
    val world_id: String? = null,
    val event_type: String? = null,

    val metagame_event_state_name: String? = null,
    val metagame_event_id_join_metagame_event: MetagameEvent?,
)
