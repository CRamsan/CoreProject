package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Stat(
    var all_time: String? = null,
    val last_save: String? = null,
    val last_save_date: String? = null,
    val one_life_max: String? = null,
    val stat_name: String? = null,
    val day: Day? = null,
    val week: Week? = null,
    val month: Month? = null,
) {
    companion object {
        fun newInstance(statName: String, allTime: String) = Stat(
            all_time = allTime,
            last_save = null,
            last_save_date = null,
            one_life_max = null,
            stat_name = statName,
            day = Day.newInstance(),
            week = Week.newInstance(),
            month = Month.newInstance(),
        )
    }
}
