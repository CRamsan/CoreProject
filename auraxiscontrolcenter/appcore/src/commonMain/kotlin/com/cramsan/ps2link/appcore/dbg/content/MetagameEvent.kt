package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi

/**
 * Created by cramsan on 9/13/15.
 */
data class MetagameEvent (
    var metagame_event_id: String? = null,
    var type: String? = null,
    var experience_bonus: String? = null,
    var name: Name_Multi? = null,
    var description: Name_Multi? = null,
)