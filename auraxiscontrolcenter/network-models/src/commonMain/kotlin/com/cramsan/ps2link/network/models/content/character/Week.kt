package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Week(
    var w01: String? = null,
    val w02: String? = null,
    val w03: String? = null,
    val w04: String? = null,
    val w05: String? = null,
    val w06: String? = null,
    val w07: String? = null,
    val w08: String? = null,
    val w09: String? = null,
    val w10: String? = null,
    val w12: String? = null,
    val w13: String? = null,
) {
    companion object {
        fun newInstance() = Week(
            w01 = null,
            w02 = null,
            w03 = null,
            w04 = null,
            w05 = null,
            w06 = null,
            w07 = null,
            w08 = null,
            w09 = null,
            w10 = null,
            w12 = null,
            w13 = null,
        )
    }
}
