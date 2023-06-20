package com.cramsan.ps2link.appfrontend.profilepager.statlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.StatItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class StatListViewModel(
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
    StatListViewModelInterface {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    private val _statList = MutableStateFlow<ImmutableList<StatItem>>(persistentListOf())
    override val statList = _statList.asStateFlow()

    private var characterId: String? = null
    private var namespace: Namespace? = null

    private var job: Job? = null

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _statList.value = persistentListOf()
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
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val characterId = characterId
            val namespace = namespace
            if (characterId == null || namespace == null) {
                logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
                loadingCompletedWithError()
                return@launch
            }
            val response = pS2LinkRepository.getStatList(characterId, namespace, lang)
            if (response.isSuccessful) {
                _statList.value = mapToUIModels(response.requireBody())
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }

    private fun mapToUIModels(response: List<StatItem>): ImmutableList<StatItem> {
        return response.map {
            it.copy(
                statName = it.statName?.replace("_", " ")?.uppercase()
            )
        }.toImmutableList()
    }
}

/**
 *
 */
interface StatListViewModelInterface : BasePS2ViewModelInterface {
    val statList: StateFlow<ImmutableList<StatItem>>
    /**
     *
     */
    fun setUp(characterId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onRefreshRequested()
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
}
