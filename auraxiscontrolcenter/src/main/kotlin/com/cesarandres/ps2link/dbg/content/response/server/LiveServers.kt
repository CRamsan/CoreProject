package com.cesarandres.ps2link.dbg.content.response.server

import com.google.gson.annotations.SerializedName

class LiveServers {
    @SerializedName("Briggs (AU)")
    var briggs: LiveServer? = null

    @SerializedName("Cobalt (EU)")
    var cobalt: LiveServer? = null

    @SerializedName("Connery (US West)")
    var connery: LiveServer? = null

    @SerializedName("Emerald (US East)")
    var emerald: LiveServer? = null

    @SerializedName("Miller (EU)")
    var miller: LiveServer? = null
}
