package com.cramsan.ps2link.appcore.dbg.content

data class WorldEvent (
    var event: EventType? = null,
    var faction_nc: String? = null,
    var faction_tr: String? = null,
    var faction_vs: String? = null,
    var metagame_event_state: String? = null,
    var metagame_event_id: String? = null,
    var timestamp: String? = null,
    var zone_id: String? = null,
    var world_id: String? = null,
    var event_type: String? = null,

    var metagame_event_state_name: String? = null,
    var metagame_event_id_join_metagame_event: MetagameEvent? = null,
)