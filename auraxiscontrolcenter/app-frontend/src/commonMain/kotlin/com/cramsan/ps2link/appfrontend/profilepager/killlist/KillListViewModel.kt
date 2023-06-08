package com.cramsan.ps2link.appfrontend.profilepager.killlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.FormatSimpleDateTime
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.OpenProfile
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KillListViewModel constructor(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    languageProvider: LanguageProvider,
    dispatcherProvider: DispatcherProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    KillListEventHandler,
    KillListViewModelInterface {

    override val logTag: String
        get() = "KillListViewModel"

    // State
    private val _killList = MutableStateFlow<ImmutableList<KillEventUIModel>>(persistentListOf())
    override val killList = _killList.asStateFlow()

    lateinit var characterId: String
    lateinit var namespace: Namespace

    override fun setUp(characterId: String?, namespace: Namespace?) {
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
        viewModelScope.launch {
            _events.emit(OpenProfile(profileId, namespace))
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val currentLang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val response = pS2LinkRepository.getKillList(characterId, namespace, currentLang)
            delay(3000)
            if (response.isSuccessful) {
                _killList.value = response.requireBody().mapToUIModel().toImmutableList()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}

private fun List<KillEvent>.mapToUIModel(): List<KillEventUIModel> {
    return map {
        KillEventUIModel(
            characterId = it.characterId,
            namespace = it.namespace,
            killType = it.killType,
            faction = it.faction,
            attacker = it.attacker,
            time = it.time?.let { instant -> FormatSimpleDateTime(instant) },
            weaponName = it.weaponName,
            weaponImage = it.weaponImage,
        )
    }
}

interface KillListViewModelInterface {
    val killList: StateFlow<ImmutableList<KillEventUIModel>>
    fun setUp(characterId: String?, namespace: Namespace?)
}
