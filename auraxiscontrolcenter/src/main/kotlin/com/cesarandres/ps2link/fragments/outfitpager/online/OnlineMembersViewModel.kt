package com.cesarandres.ps2link.fragments.outfitpager.online

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnlineMembersViewModel @Inject constructor(
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
    OnlineMemberEventHandler {

    override val logTag: String
        get() = "OnlineMembersViewModel"

    // State
    private val _memberList = MutableStateFlow<List<Character>>(emptyList())
    val memberList = _memberList.asStateFlow()

    fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            loadingCompletedWithError()
            return
        }
        loadingStarted()
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            val response = pS2LinkRepository.getMembersOnline(outfitId, namespace, lang)
            if (response.isSuccessful) {
                _memberList.value = response.requireBody().sortedBy { it.name }
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }
}
