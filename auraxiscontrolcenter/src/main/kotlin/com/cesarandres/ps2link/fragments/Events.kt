package com.cesarandres.ps2link.fragments

import com.cesarandres.ps2link.fragments.outfitpager.FragmentOutfitPagerArgs
import com.cesarandres.ps2link.fragments.profilepager.FragmentProfilePagerArgs
import com.cramsan.framework.core.BaseEvent
import com.cramsan.ps2link.core.models.Namespace

data class OpenProfile(val characterId: String, val namespace: Namespace) : BaseEvent() {
    val args = FragmentProfilePagerArgs(characterId, namespace)
}
data class OpenOutfit(val outfitId: String, val namespace: Namespace) : BaseEvent() {
    val args = FragmentOutfitPagerArgs(outfitId, namespace)
}
// TODO: Enforce URL requirements
data class OpenUrl(val url: String) : BaseEvent()

object OpenProfileList : BaseEvent()
object OpenOutfitList : BaseEvent()
object OpenServerList : BaseEvent()
object OpenTwitter : BaseEvent()
object OpenReddit : BaseEvent()
object OpenAbout : BaseEvent()
