package com.cramsan.ps2link.appcore.dbg.content.response.server

import kotlinx.serialization.SerialName

data class LiveServers (
    @SerialName("Briggs (AU)")
    var briggs: LiveServer? = null,

    @SerialName("Cobalt (EU)")
    var cobalt: LiveServer? = null,

    @SerialName("Connery (US West)")
    var connery: LiveServer? = null,

    @SerialName("Emerald (US East)")
    var emerald: LiveServer? = null,

    @SerialName("Miller (EU)")
    var miller: LiveServer? = null,
)