package com.cesarandres.ps2link

import com.cesarandres.ps2link.deprecated.module.ObjectDataSource
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appfrontend.BasePS2AndroidViewModel
import com.cramsan.ps2link.appfrontend.NoopPS2ViewModel
import com.cramsan.ps2link.core.models.CensusLang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UndocumentedPublicClass")
@HiltViewModel
class ActivityContainerViewModel @Inject constructor(
    private val objectDataSource: ObjectDataSource,
    viewModel: NoopPS2ViewModel,
) : BasePS2AndroidViewModel<NoopPS2ViewModel>(
    viewModel,
) {

    override val logTag = "ActivityContainerViewModel"

    fun setUp() {
        val lang = getCurrentLang()
        viewModelScope.launch(dispatcherProvider.ioDispatcher()) {
            ps2Settings.updateCurrentLang(lang)
            logEvent(logTag, "Language Set", mapOf("Lang" to lang.name))

            migrateDatabase(lang)
        }
    }

    private suspend fun migrateDatabase(lang: CensusLang) {
        // Perform migration
        objectDataSource.open()
        val profiles = objectDataSource.getAllCharacterProfiles(false)
        val outfits = objectDataSource.getAllOutfits(false)
        profiles.forEach { cachedProfiles ->
            val response = pS2LinkRepository.getCharacter(cachedProfiles.characterId, cachedProfiles.namespace, lang)
            if (response.isSuccessfulAndContainsBody()) {
                response.requireBody()?.let {
                    pS2LinkRepository.saveCharacter(it.copy(cached = true))
                }
            }
        }
        outfits.forEach {
            val response = pS2LinkRepository.getOutfit(it.id, it.namespace, lang)
            if (response.isSuccessful) {
                pS2LinkRepository.saveOutfit(response.requireBody().copy(cached = true))
            }
        }

        if (profiles.isNotEmpty()) {
            objectDataSource.deleteAllCharacterProfiles(true)
        }
        if (outfits.isNotEmpty()) {
            objectDataSource.deleteAllOutfit(true)
        }
        objectDataSource.close()
    }
}
