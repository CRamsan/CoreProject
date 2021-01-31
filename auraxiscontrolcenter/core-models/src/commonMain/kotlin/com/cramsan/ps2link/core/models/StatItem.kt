package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/26/2021
 */
data class StatItem(
    var statName: String? = null,
    var allTime: Double? = null,
    var today: Double? = null,
    var thisWeek: Double? = null,
    var thisMonth: Double? = null,
)
