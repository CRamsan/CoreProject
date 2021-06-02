package com.cesarandres.ps2link.fragments.profilepager.weaponlist

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.getCurrentLang
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.Faction
import com.cramsan.ps2link.core.models.Namespace
import com.cramsan.ps2link.core.models.WeaponEventType
import com.cramsan.ps2link.core.models.WeaponItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeaponListViewModel @Inject constructor(
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
    WeaponListEventHandler {

    override val logTag: String
        get() = "WeaponListViewModel"

    // State
    private val _weaponList = MutableStateFlow<List<WeaponItem>>(emptyList())
    val weaponList = _weaponList.asStateFlow()

    private val _faction = MutableStateFlow<Faction>(Faction.UNKNOWN)
    val faction = _faction.asStateFlow()

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

    override fun onRefreshRequested() {
        loadingStarted()
        ioScope.launch {
            val currentLang = ps2Settings.getCurrentLang() ?: getCurrentLang()
            val response = pS2LinkRepository.getWeaponList(characterId, namespace, currentLang)
            if (response.isSuccessful) {
                _weaponList.value = response.requireBody().filter {
                    (
                        it.statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
                            ?: 0
                        ) > 0
                }.sortedByDescending {
                    it.statMapping[WeaponEventType.KILLS]?.stats?.values?.filterNotNull()?.sum()
                        ?: 0
                }
                loadingCompleted()
            } else {
                loadingCompletedWithError()
            }
        }
    }
}
