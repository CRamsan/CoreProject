package com.cramsan.ps2link.appfrontend.addoutfit

import com.cramsan.ps2link.core.models.Outfit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface OutfitAddViewModelInterface {
    val outfitList: StateFlow<ImmutableList<Outfit>>
    val tagSearchQuery: StateFlow<String>
    val nameSearchQuery: StateFlow<String>
    fun search(tag: String, name: String)
}
