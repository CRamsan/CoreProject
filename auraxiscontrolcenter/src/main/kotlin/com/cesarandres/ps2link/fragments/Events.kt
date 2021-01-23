package com.cesarandres.ps2link.fragments

import com.cramsan.framework.core.BaseEvent
import com.cramsan.ps2link.appcore.dbg.Namespace

data class OpenProfile(val characterId: String, val namespace: Namespace) : BaseEvent()
data class OpenOutfit(val outfitId: String, val namespace: Namespace) : BaseEvent()
object OpenProfileList : BaseEvent()
object OpenProfileSearch : BaseEvent()
object OpenOutfitList : BaseEvent()
object OpenServerList : BaseEvent()
object OpenTwitter : BaseEvent()
object OpenReddit : BaseEvent()
object OpenAbout : BaseEvent()
