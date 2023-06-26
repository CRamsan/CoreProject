package com.cramsan.ps2link.appfrontend

import com.cramsan.framework.core.BaseEvent
import com.cramsan.ps2link.core.models.Namespace

/**
 *
 */
sealed class BasePS2Event : BaseEvent() {
    /**
     *
     */
    data class OpenProfile(val characterId: String, val namespace: Namespace) : BasePS2Event()

    /**
     *
     */
    data class OpenOutfit(val outfitId: String, val namespace: Namespace) : BasePS2Event()

    /**
     *
     */
    data class OpenUrl(val url: String) : BasePS2Event()

    /**
     *
     */
    object OpenProfileList : BasePS2Event()
    /**
     *
     */
    object OpenOutfitList : BasePS2Event()
    /**
     *
     */
    object OpenServerList : BasePS2Event()
    /**
     *
     */
    object OpenTwitter : BasePS2Event()
    /**
     *
     */
    object OpenReddit : BasePS2Event()
    /**
     *
     */
    object OpenAbout : BasePS2Event()

    /**
     *
     */
    object OpenSettings : BasePS2Event()

    /**
     *
     */
    data class OpenProfileLiveTracker(val characterId: String, val namespace: Namespace) : BasePS2Event()

    /**
     *
     */
    data class OpenOutfitLiveTracker(val outfitId: String, val namespace: Namespace) : BasePS2Event()
}
