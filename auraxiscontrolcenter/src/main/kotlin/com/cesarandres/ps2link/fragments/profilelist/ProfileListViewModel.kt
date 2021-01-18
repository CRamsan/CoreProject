package com.cesarandres.ps2link.fragments.profilelist

import android.app.Application
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.fragments.OpenProfile
import com.cesarandres.ps2link.fragments.OpenProfileSearch
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.ps2link.appcore.DBGServiceClient
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.sqldelight.DbgDAO

class ProfileListViewModel @ViewModelInject constructor(
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
    ProfileListEventHandler {

    override val logTag: String
        get() = "ProfileListViewModel"

    // State
    private val _profileList = dbgDAO.getAllCharactersAsFlow()
    val profileList = _profileList.asLiveData()

    override fun onSearchProfileClick() {
        events.value = OpenProfileSearch
    }

    override fun onProfileSelected(profileId: String) {
        events.value = OpenProfile(profileId)
    }
}
