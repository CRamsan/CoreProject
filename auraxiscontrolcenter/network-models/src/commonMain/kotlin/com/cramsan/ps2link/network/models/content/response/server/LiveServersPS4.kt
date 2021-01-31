package com.cramsan.ps2link.network.models.content.response.server

import kotlinx.serialization.SerialName

class LiveServersPS4 {

    @SerialName("Ceres (EU)")
    var ceres: LiveServer? = null

    @SerialName("Crux")
    var crux: LiveServer? = null

    @SerialName("Dahaka (EU)")
    var dahaka: LiveServer? = null

    @SerialName("Genudine")
    var genudine: LiveServer? = null

    @SerialName("Lithcorp (EU)")
    var lithcorp: LiveServer? = null

    @SerialName("Palos")
    var palos: LiveServer? = null

    @SerialName("Rashnu (EU)")
    var rashnu: LiveServer? = null

    @SerialName("Searhus")
    var searhus: LiveServer? = null
}
