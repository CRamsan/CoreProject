package com.cesarandres.ps2link.fragments.profilepager

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.db.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfilePagerViewModel @ViewModelInject constructor(
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
) {

    override val logTag: String
        get() = "ProfilePagerViewModel"

    // State
    lateinit var profile: Flow<Character?>
    private lateinit var characterId: String
    private lateinit var namespace: Namespace

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }
        this.characterId = characterId
        this.namespace = namespace
        profile = dbgDAO.getCharacterAsFlow(characterId, namespace)
    }

    fun addCharacter() {
        ioScope.launch {
            val character = dbgDAO.getCharacter(characterId, namespace)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            dbgDAO.insertCharacter(character.copy(cached = true))
        }
    }

    fun removeCharacter() {
        ioScope.launch {
            val character = dbgDAO.getCharacter(characterId, namespace)
            if (character == null) {
                // TODO : Report error
                return@launch
            }
            dbgDAO.insertCharacter(character.copy(cached = false))
        }
    }

    fun pinCharacter() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(namespace)
            ps2Settings.updatePreferredCharacterId(characterId)
        }
    }

    fun unpinCharacter() {
        ioScope.launch {
            ps2Settings.updatePreferredNamespace(null)
            ps2Settings.updatePreferredCharacterId(null)
        }
    }
}
