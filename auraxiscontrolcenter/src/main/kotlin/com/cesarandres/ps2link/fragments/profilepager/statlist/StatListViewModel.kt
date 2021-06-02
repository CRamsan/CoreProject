package com.cesarandres.ps2link.fragments.profilepager.statlist

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
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.StatItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatListViewModel @Inject constructor(
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
    StatListEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    private val _statList = MutableStateFlow<List<StatItem>>(emptyList())
    val statList = _statList.asStateFlow()

    private lateinit var characterId: String
    private lateinit var namespace: Namespace

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            loadingCompletedWithError()
            return
        }

        this.characterId = characterId
        this.namespace = namespace
        onRefreshRequested()
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }

    override fun onRefreshRequested() {
        loadingStarted()
        ioScope.launch {
            val lang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            val response = pS2LinkRepository.getStatList(characterId, namespace, lang)
            if (response.isSuccessful) {
                _statList.value = response.requireBody()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}
