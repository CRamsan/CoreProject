package com.cramsan.ps2link.network.models.content.character

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    var m01: String? = null,
    val m02: String? = null,
    val m03: String? = null,
    val m04: String? = null,
    val m05: String? = null,
    val m06: String? = null,
    val m07: String? = null,
    val m08: String? = null,
    val m09: String? = null,
    val m10: String? = null,
    val m12: String? = null,
) {
    companion object {
        fun newInstance() = Month(
            m01 = null,
            m02 = null,
            m03 = null,
            m04 = null,
            m05 = null,
            m06 = null,
            m07 = null,
            m08 = null,
            m09 = null,
            m10 = null,
            m12 = null,
        )
    }
}
