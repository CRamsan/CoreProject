package com.cramsan.ps2link.appcore.dbg.content.response.server

import kotlinx.serialization.SerialName

data class PS2 (
    @SerialName("Live")
    var live: LiveServers? = null,

    @SerialName("Live PS4")
    var livePS4: LiveServersPS4? = null,
)
