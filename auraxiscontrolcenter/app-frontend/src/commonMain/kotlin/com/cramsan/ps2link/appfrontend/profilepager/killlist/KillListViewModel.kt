package com.cramsan.ps2link.appfrontend.profilepager.killlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.appfrontend.formatSimpleDateTime
import com.cramsan.ps2link.core.models.KillEvent
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class KillListViewModel(
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
    KillListViewModelInterface {

    override val logTag: String
        get() = "KillListViewModel"

    // State
    private val _killList = MutableStateFlow<ImmutableList<KillEventUIModel>>(persistentListOf())
    override val killList = _killList.asStateFlow()

    private var characterId: String? = null
    private var namespace: Namespace? = null

    private var job: Job? = null

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _killList.value = persistentListOf()
        }

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
            _events.emit(BasePS2Event.OpenProfile(profileId, namespace))
        }
    }

    override fun onRefreshRequested() {
        loadingStarted()
        job?.cancel()
        job = viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val characterId = characterId
            val namespace = namespace
            if (characterId == null || namespace == null) {
                logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
                loadingCompletedWithError()
                return@launch
            }
            val currentLang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val response = pS2LinkRepository.getKillList(characterId, namespace, currentLang)
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
            time = it.time?.let { instant -> formatSimpleDateTime(instant) },
            weaponName = it.weaponName,
            weaponImage = it.weaponImage,
        )
    }
}

/**
 *
 */
interface KillListViewModelInterface : BasePS2ViewModelInterface {
    val killList: StateFlow<ImmutableList<KillEventUIModel>>
    /**
     *
     */
    fun setUp(characterId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}
