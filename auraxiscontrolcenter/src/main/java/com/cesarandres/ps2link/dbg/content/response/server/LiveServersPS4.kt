package com.cesarandres.ps2link.dbg.content.response.server

import com.google.gson.annotations.SerializedName

class LiveServersPS4 {

    @SerializedName("Ceres (EU)")
    var ceres: LiveServer? = null

    @SerializedName("Crux")
    var crux: LiveServer? = null

    @SerializedName("Dahaka (EU)")
    var dahaka: LiveServer? = null

    @SerializedName("Genudine")
    var genudine: LiveServer? = null

    @SerializedName("Lithcorp (EU)")
    var lithcorp: LiveServer? = null

    @SerializedName("Palos")
    var palos: LiveServer? = null

    @SerializedName("Rashnu (EU)")
    var rashnu: LiveServer? = null

    @SerializedName("Searhus")
    var searhus: LiveServer? = null
}
