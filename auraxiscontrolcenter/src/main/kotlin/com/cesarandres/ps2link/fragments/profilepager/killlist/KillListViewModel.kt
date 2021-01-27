package com.cesarandres.ps2link.fragments.profilepager.killlist

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.toUIFaction
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Faction
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.dbg.content.CharacterEvent
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.ui.KillType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KillListViewModel @ViewModelInject constructor(
    application: Application,
    dbgServiceClient: DBGServiceClient,
    dbgDAO: DbgDAO,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    @Assisted savedStateHandle: SavedStateHandle,
) : BasePS2ViewModel(
        application,
        dbgServiceClient,
        dbgDAO,
        pS2Settings,
        dispatcherProvider,
        savedStateHandle
    ),
    KillListEventHandler {

    override val logTag: String
        get() = "KillListViewModel"

    // State
    private val _killList = MutableStateFlow<List<KillEvent>>(emptyList())
    val killList = _killList.asStateFlow()

    lateinit var characterId: String

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }

        this.characterId = characterId
        ioScope.launch {
            val killListResponse = dbgCensus.getKillList(
                character_id = characterId,
                namespace = namespace,
                currentLang = ps2Settings.getCurrentLang() ?: CensusLang.EN,
            )
            _killList.value = formatKillList(killListResponse, namespace)
        }
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }

    fun formatKillList(characterEventList: List<CharacterEvent>?, namespace: Namespace): List<KillEvent> {
        if (characterEventList == null) {
            return emptyList()
        }

        return characterEventList.map {
            val weaponName = it.weapon_name
            val attackerName: String?
            val time = it.timestamp?.toLong()
            val killType: KillType
            var characterId: String? = null
            val faction: Faction = Faction.fromString(it.attacker?.faction_id)

            if (it.attacker_character_id == this.characterId) {
                attackerName = it.character?.name?.first
                it.important_character_id = it.character_id
                if (it.character_id == this.characterId) {
                    killType = KillType.SUICIDE
                } else {
                    killType = KillType.KILL
                    characterId = it.character_id
                }
            } else if (it.character_id == this.characterId) {
                killType = KillType.KILLEDBY
                attackerName = it.attacker?.name?.first
                characterId = it.attacker_character_id
                it.important_character_id = it.attacker_character_id
            } else {
                killType = KillType.UNKNOWN
                attackerName = null
            }

            KillEvent(
                characterId = characterId,
                namespace = namespace,
                killType = killType,
                faction = faction.toUIFaction(),
                attacker = attackerName,
                time = time,
                weaponName = weaponName,
            )
        }
    }
}
