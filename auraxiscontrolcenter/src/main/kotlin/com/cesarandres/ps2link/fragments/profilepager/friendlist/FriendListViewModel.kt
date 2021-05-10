package com.cesarandres.ps2link.fragments.profilepager.friendlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    application: Application,
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
    application,
    pS2LinkRepository,
    pS2Settings,
    dispatcherProvider,
    savedStateHandle
),
    FriendListEventHandler {

    override val logTag: String
        get() = "FriendListViewModel"

    // State
    private val _friendList = MutableStateFlow<List<Character>>(emptyList())
    val friendList = _friendList.asStateFlow()

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }

        ioScope.launch {
            val currentLang = ps2Settings.getCurrentLang() ?: CensusLang.EN
            _friendList.value = pS2LinkRepository.getFriendList(characterId, namespace, currentLang)
        }
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }
}
