package com.cramsan.ps2link.appfrontend.profilepager.friendlist

import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.logging.logE
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.appfrontend.BasePS2Event
import com.cramsan.ps2link.appfrontend.BasePS2ViewModel
import com.cramsan.ps2link.appfrontend.BasePS2ViewModelInterface
import com.cramsan.ps2link.appfrontend.LanguageProvider
import com.cramsan.ps2link.core.models.Character
import com.cramsan.ps2link.core.models.LoginStatus
import com.cramsan.ps2link.core.models.Namespace
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
class FriendListViewModel(
    pS2LinkRepository: PS2LinkRepository,
    pS2Settings: PS2Settings,
    dispatcherProvider: DispatcherProvider,
    languageProvider: LanguageProvider,
) : BasePS2ViewModel(
    pS2LinkRepository,
    pS2Settings,
    languageProvider,
    dispatcherProvider,
),
    FriendListViewModelInterface {

    override val logTag: String
        get() = "FriendListViewModel"

    // State
    private val _friendList = MutableStateFlow<ImmutableList<Character>>(persistentListOf())
    override val friendList = _friendList.asStateFlow()

    private var characterId: String? = null
    private var namespace: Namespace? = null

    private var job: Job? = null

    override fun setUp(characterId: String?, namespace: Namespace?) {
        if (this.characterId != characterId || this.namespace != namespace) {
            _friendList.value = persistentListOf()
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
            val currentLang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val characterId = characterId
            val namespace = namespace
            if (characterId == null || namespace == null) {
                logE(logTag, "Invalid arguments: characterId=$characterId namespace=$namespace")
                loadingCompletedWithError()
                return@launch
            }
            val response = pS2LinkRepository.getFriendList(characterId, namespace, currentLang)
            if (response.isSuccessful) {
                _friendList.value = response.requireBody().sortedWith { object1, object2 ->
                    if (object1.loginStatus == object2.loginStatus) {
                        (object1.name ?: "").compareTo(object2.name ?: "")
                    } else {
                        if (object1.loginStatus == LoginStatus.ONLINE) {
                            -1
                        } else {
                            1
                        }
                    }
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
interface FriendListViewModelInterface : BasePS2ViewModelInterface {
    val friendList: StateFlow<ImmutableList<Character>>
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
