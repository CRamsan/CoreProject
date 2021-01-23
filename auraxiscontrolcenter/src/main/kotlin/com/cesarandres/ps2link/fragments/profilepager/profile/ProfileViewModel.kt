package com.cesarandres.ps2link.fragments.profilepager.profile

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenOutfit
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import com.cramsan.ps2link.db.Character
import kotlinx.coroutines.flow.Flow

class ProfileViewModel @ViewModelInject constructor(
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
    ProfileEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    lateinit var profile: Flow<Character?>

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }
        profile = dbgDAO.getCharacterAsFlow(characterId, namespace)
    }

    override fun onOutfitSelected(outfitId: String, namespace: Namespace) {
        events.value = OpenOutfit(outfitId, namespace)
    }
}
