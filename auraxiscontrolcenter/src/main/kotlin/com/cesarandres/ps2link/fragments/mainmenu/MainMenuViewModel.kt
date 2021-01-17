package com.cesarandres.ps2link.fragments.mainmenu

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.fragments.OpenAbout
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.OpenOutfitList
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileList
import com.cesarandres.ps2link.fragments.OpenReddit
import com.cesarandres.ps2link.fragments.OpenServerList
import com.cesarandres.ps2link.fragments.OpenTwitter
import com.cramsan.framework.core.BaseViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.dbg.content.CharacterProfile
import com.cramsan.ps2link.appcore.dbg.content.Outfit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MainMenuViewModel @ViewModelInject constructor(
    application: Application,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(application, dispatcherProvider, savedStateHandle), MainMenuEventHandler {

    override val logTag: String
        get() = "MainMenuViewModel"

    // State
    private val _preferredProfile = MutableStateFlow<CharacterProfile?>(null)
    private val _preferredOutfit = MutableStateFlow<Outfit?>(null)
    private val _preferredProfileName = _preferredProfile.map {
        it?.name?.first
    }.flowOn(dispatcherProvider.mainDispatcher())
    private val _preferredOutfitName = _preferredOutfit.map {
        it?.name
    }.flowOn(dispatcherProvider.mainDispatcher())
    val preferredProfileName = _preferredProfileName.asLiveData()
    val preferredOutfileName = _preferredOutfitName.asLiveData()

    override fun onPreferredProfileClick() {
        _preferredProfile.value?.let {
            events.value = OpenProfile(it.character_id)
        }
    }

    override fun onPreferredOutfitClick() {
        _preferredOutfit.value?.let {
            events.value = OpenOutfit(it.outfit_id)
        }
    }

    override fun onProfileClick() {
        events.value = OpenProfileList
    }

    override fun onServersClick() {
        events.value = OpenServerList
    }

    override fun onOutfitsClick() {
        events.value = OpenOutfitList
    }

    override fun onTwitterClick() {
        events.value = OpenTwitter
    }

    override fun onRedditClick() {
        events.value = OpenReddit
    }

    override fun onAboutClick() {
        events.value = OpenAbout
    }
}
