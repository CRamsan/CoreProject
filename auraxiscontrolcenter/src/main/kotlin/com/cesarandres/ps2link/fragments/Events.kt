package com.cesarandres.ps2link.fragments

import com.cramsan.framework.core.BaseEvent

data class OpenProfile(val characterId: String) : BaseEvent()
data class OpenOutfit(val outfitId: String) : BaseEvent()
object OpenProfileList : BaseEvent()
object OpenOutfitList : BaseEvent()
object OpenServerList : BaseEvent()
object OpenTwitter : BaseEvent()
object OpenReddit : BaseEvent()
object OpenAbout : BaseEvent()
