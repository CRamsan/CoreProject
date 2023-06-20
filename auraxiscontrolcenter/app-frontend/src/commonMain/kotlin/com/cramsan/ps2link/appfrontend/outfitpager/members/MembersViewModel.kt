package com.cramsan.ps2link.appfrontend.outfitpager.members

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
/**
 *
 */
class MembersViewModel(
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
    MembersViewModelInterface {

    override val logTag: String
        get() = "OnlineMembersViewModel"

    // State
    private val _memberList = MutableStateFlow<ImmutableList<Character>>(persistentListOf())
    override val memberList = _memberList.asStateFlow()

    private lateinit var outfitId: String
    private lateinit var namespace: Namespace

    override fun setUp(outfitId: String?, namespace: Namespace?) {
        if (outfitId == null || namespace == null) {
            logE(logTag, "Invalid arguments: outfitId=$outfitId namespace=$namespace")
            loadingCompletedWithError()
            return
        }
        this.outfitId = outfitId
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
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            val lang = ps2Settings.getCurrentLang() ?: languageProvider.getCurrentLang()
            val response = pS2LinkRepository.getMembers(outfitId, namespace, lang)
            if (response.isSuccessful) {
                _memberList.value = response.requireBody().sortedWith { object1, object2 ->
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
interface MembersViewModelInterface : BasePS2ViewModelInterface {
    val memberList: StateFlow<ImmutableList<Character>>
    /**
     *
     */
    fun setUp(outfitId: String?, namespace: Namespace?)
    /**
     *
     */
    fun onProfileSelected(profileId: String, namespace: Namespace)
    /**
     *
     */
    fun onRefreshRequested()
}
