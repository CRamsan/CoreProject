package com.cramsan.ps2link.appfrontend.addoutfit

import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.Outfit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

/**
 *
 */
interface OutfitAddViewModelInterface : BasePS2ViewModelInterface {
    val outfitList: StateFlow<ImmutableList<Outfit>>
    val tagSearchQuery: StateFlow<String>
    val nameSearchQuery: StateFlow<String>
    /**
     *
     */
    fun search(tag: String, name: String)
    /**
     *
     */
    fun onTagFieldUpdated(searchField: String)
    /**
     *
     */
    fun onNameFieldUpdated(searchField: String)
    /**
     *
     */
    fun onOutfitSelected(outfitId: String, namespace: Namespace)
}
