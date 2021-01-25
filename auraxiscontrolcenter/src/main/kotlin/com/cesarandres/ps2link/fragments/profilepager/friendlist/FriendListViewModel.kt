package com.cesarandres.ps2link.fragments.profilepager.friendlist

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.LoginStatus
import com.cramsan.ps2link.appcore.dbg.Namespace
import com.cramsan.ps2link.appcore.models.FriendCharacter
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendListViewModel @ViewModelInject constructor(
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
    FriendListEventHandler {

    override val logTag: String
        get() = "ProfileViewModel"

    // State
    private val _friendList = MutableStateFlow<List<FriendCharacter>>(emptyList())
    val friendList = _friendList.asStateFlow()

    fun setUp(characterId: String?, namespace: Namespace?) {
        if (characterId == null || namespace == null) {
            logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
            // TODO: Provide some event that can be handled by the UI
            return
        }

        ioScope.launch {
            val friendListResponse = dbgCensus.getFriendList(
                character_id = characterId,
                namespace = namespace,
                currentLang = ps2Settings.getCurrentLang() ?: CensusLang.EN,
            )
            _friendList.value = friendListResponse?.map { friend ->
                friend.character_id?.let { id ->
                    FriendCharacter(
                        id,
                        friend.name?.first,
                        namespace,
                        LoginStatus.fromString(friend.online.toString())
                    )
                }
            }?.filterNotNull() ?: emptyList()
        }
    }

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        events.value = OpenProfile(profileId, namespace)
    }
}
