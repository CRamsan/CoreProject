package com.cramsan.ps2link.network.ws.testgui.ui.tabs

import com.cramsan.ps2link.core.models.Namespace

sealed class ApplicationTab {

    data class Profile(
        val characterId: String?,
        val namespace: Namespace?,
    ) : ApplicationTab()

    data class Outfit(
        val outfitId: String?,
        val namespace: Namespace?,
    ) : ApplicationTab()

    object Settings : ApplicationTab()
}
