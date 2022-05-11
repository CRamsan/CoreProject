package com.cesarandres.ps2link

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.cesarandres.ps2link.base.BasePS2ViewModel
import com.cesarandres.ps2link.deprecated.module.ObjectDataSource
import com.cramsan.framework.core.DispatcherProvider
import com.cramsan.framework.userevents.logEvent
import com.cramsan.ps2link.appcore.network.isSuccessfulAndContainsBody
import com.cramsan.ps2link.appcore.network.requireBody
import com.cramsan.ps2link.appcore.preferences.PS2Settings
import com.cramsan.ps2link.appcore.repository.PS2LinkRepository
import com.cramsan.ps2link.core.models.CensusLang
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@HiltViewModel
class ActivityContainerViewModel @Inject constructor(
    private val objectDataSource: ObjectDataSource,
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
    savedStateHandle,
) {

    override val logTag: String
        get() = "ActivityContainerViewModel"

    fun setUp() {
        val lang = getCurrentLang()
        ioScope.launch {
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
                @OptIn(ExperimentalTime::class)
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
