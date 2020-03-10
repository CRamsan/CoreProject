package com.cesarandres.ps2link.dbg.content.response.server

import com.google.gson.annotations.SerializedName

class PS2 {
    @SerializedName("Live")
    var live: LiveServers? = null

    @SerializedName("Live PS4")
    var livePS4: LiveServersPS4? = null
}
