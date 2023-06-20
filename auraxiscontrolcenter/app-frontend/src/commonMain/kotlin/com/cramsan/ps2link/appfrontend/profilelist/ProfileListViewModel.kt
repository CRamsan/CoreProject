package com.cramsan.ps2link.appfrontend.profilelist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.thread.assertIsBackgroundThread
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.Namespace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 *
 */
class ProfileListViewModel(
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
    ProfileListEventHandler,
    ProfileListViewModelInterface {

    override val logTag: String
        get() = "ProfileListViewModel"

    // State
    override val profileList = pS2LinkRepository.getAllCharactersAsFlow().map {
        assertIsBackgroundThread()
        it.sortedBy { character -> character.name?.lowercase() }.toImmutableList()
    }.flowOn(dispatcherProvider.ioDispatcher())

    override fun onProfileSelected(profileId: String, namespace: Namespace) {
        viewModelScope.launch {
            _events.emit(BasePS2Event.OpenProfile(profileId, namespace))
        }
    }
}

/**
 *
 */
interface ProfileListViewModelInterface : BasePS2ViewModelInterface {
    // State
    val profileList: Flow<ImmutableList<Character>>
}
