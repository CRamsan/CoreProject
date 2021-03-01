package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Stat(
    var all_time: String? = null,
    var last_save: String? = null,
    var last_save_date: String? = null,
    var one_life_max: String? = null,
    var stat_name: String? = null,
    var day: Day? = null,
    var week: Week? = null,
    var month: Month? = null,
)
