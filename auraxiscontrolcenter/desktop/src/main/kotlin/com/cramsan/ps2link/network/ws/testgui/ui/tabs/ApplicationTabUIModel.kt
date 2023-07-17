package com.cramsan.ps2link.network.ws.testgui.ui.tabs

import com.cramsan.ps2link.core.models.Namespace

sealed class ApplicationTabUIModel {

    data class Profile(
        val characterId: String?,
        val namespace: Namespace?,
        val showFTE: Boolean,
    ) : ApplicationTabUIModel()

    data class Outfit(
        val outfitId: String?,
        val namespace: Namespace?,
        val showFTE: Boolean,
    ) : ApplicationTabUIModel()

    object Settings : ApplicationTabUIModel()

    data class Tracker(
        val characterId: String?,
        val namespace: Namespace?,
        val showFTE: Boolean,
    ) : ApplicationTabUIModel()
}
