package com.cramsan.ps2link.appfrontend.profilepager.weaponlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 *
 */
class WeaponListViewModel(
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
    WeaponListViewModelInterface {

    override val logTag: String
        get() = "WeaponListViewModel"

    // State
    private val _weaponList = MutableStateFlow<ImmutableList<WeaponItem>>(persistentListOf())
    override val weaponList = _weaponList.asStateFlow()

    private val _faction = MutableStateFlow<Faction>(Faction.UNKNOWN)
    override val faction = _faction.asStateFlow()

    private var characterId: String? = null
    private var namespace: Namespace? = null

    private var job: Job? = null

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _weaponList.value = persistentListOf()
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

    override fun onRefreshRequested() {
        loadingStarted()
        job?.cancel()
        job = viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val currentLang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val characterId = characterId
            val namespace = namespace
            if (characterId == null || namespace == null) {
                logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
                loadingCompletedWithError()
                return@launch
            }
            val response = pS2LinkRepository.getWeaponList(characterId, namespace, currentLang)
            if (response.isSuccessfulAndContainsBody()) {
                _weaponList.value = response.requireBody().filter {
                    (
                        it.statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
                            ?: 0
                        ) > 0
                }.sortedByDescending {
                    it.statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
                        ?: 0
                }.toImmutableList()
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}

/**
 *
 */
interface WeaponListViewModelInterface : BasePS2ViewModelInterface {
    val weaponList: StateFlow<ImmutableList<WeaponItem>>

    val faction: StateFlow<Faction>
    /**
     *
     */
    fun setUp(characterId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onRefreshRequested()
}
