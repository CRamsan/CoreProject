package com.cesarandres.ps2link.fragments.mainmenu

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenAbout
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cesarandres.ps2link.fragments.OpenOutfitList
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileList
import com.cesarandres.ps2link.fragments.OpenReddit
import com.cesarandres.ps2link.fragments.OpenServerList
import com.cesarandres.ps2link.fragments.OpenTwitter
import com.cramsan.framework.assert.assertNotNull
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MainMenuViewModel @ViewModelInject constructor(
    application: Application,
    dbgCensus: DBGServiceClient,
    dbgDAO: DbgDAO,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle
) : BasePS2ViewModel(
        application,
        dbgCensus,
        dbgDAO,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    MainMenuEventHandler {

    override val logTag: String
        get() = "MainMenuViewModel"

    // State
    private val _preferredProfileId = MutableStateFlow<String?>(null)
    private val _preferredOutfitId = MutableStateFlow<String?>(null)

    private val _preferredProfile = _preferredProfileId.map { profileId ->
        profileId?.let {
            val namespace = ps2Settings.getPreferredNamespace()
            val lang = ps2Settings.getPreferredLang()

            assertNotNull(namespace, logTag, "Namespace cannot be null")
            assertNotNull(lang, logTag, "CensusLang cannot be null")

            if (namespace != null && lang != null) {
                dbgCensus.getProfile(it, namespace, lang)
            } else {
                null
            }
        }
    }
    private val _preferredOutfit = _preferredOutfitId.map { outfitId ->
        outfitId?.let {
            val namespace = ps2Settings.getPreferredNamespace()
            val lang = ps2Settings.getPreferredLang()

            assertNotNull(namespace, logTag, "Namespace cannot be null")
            assertNotNull(lang, logTag, "CensusLang cannot be null")

            if (namespace != null && lang != null) {
                dbgCensus.getOutfit(it, namespace, lang)
            } else {
                null
            }
        }
    }

    private val _preferredProfileName = _preferredProfile.map {
        it?.name?.first
    }
    private val _preferredOutfitName = _preferredOutfit.map {
        it?.name
    }

    val preferredProfileName = _preferredProfileName.asLiveData()
    val preferredOutfileName = _preferredOutfitName.asLiveData()

    override fun onPreferredProfileClick() {
        _preferredProfileId.value?.let {
            events.value = OpenProfile(it)
        }
    }

    override fun onPreferredOutfitClick() {
        _preferredOutfitId.value?.let {
            events.value = OpenOutfit(it)
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
