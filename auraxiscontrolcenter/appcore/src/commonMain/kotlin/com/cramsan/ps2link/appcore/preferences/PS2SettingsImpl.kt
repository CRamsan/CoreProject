package com.cramsan.ps2link.appcore.preferences

import com.cramsan.framework.preferences.Preferences
import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace

class PS2SettingsImpl(
    val preferences: Preferences
) : PS2Settings {
    override suspend fun updatePreferredCharacterId(characterId: String?) {
        preferences.saveString(PREFERRED_CHARACTER_ID, characterId)
    }

    override suspend fun getPreferredCharacterId(): String? {
        return preferences.loadString(PREFERRED_CHARACTER_ID)
    }

    override suspend fun updatePreferredOutfitId(outfitId: String?) {
        preferences.saveString(PREFERRED_OUTFIT_ID, outfitId)
    }

    override suspend fun getPreferredOutfitId(): String? {
        return preferences.loadString(PREFERRED_OUTFIT_ID)
    }

    override suspend fun updatePreferredNamespace(namespace: Namespace?) {
        preferences.saveString(PREFERRED_NAMESPACE, namespace?.name)
    }

    override suspend fun getPreferredNamespace(): Namespace? {
        return preferences.loadString(PREFERRED_NAMESPACE)?.let { Namespace.valueOf(it) }
    }

    override suspend fun updatePreferredLang(currentLang: CensusLang?) {
        preferences.saveString(PREFERRED_CENSUS_LANG, currentLang?.name)
    }

    override suspend fun getPreferredLang(): CensusLang? {
        return preferences.loadString(PREFERRED_CENSUS_LANG)?.let { CensusLang.valueOf(it) }
    }

    override suspend fun updateCurrentNamespace(namespace: Namespace?) {
        preferences.saveString(CURRENT_NAMESPACE, namespace?.name)
    }

    override suspend fun getCurrentNamespace(): Namespace? {
        return preferences.loadString(CURRENT_NAMESPACE)?.let { Namespace.valueOf(it) }
    }

    override suspend fun updateCurrentLang(currentLang: CensusLang?) {
        preferences.saveString(CURRENT_CENSUS_LANG, currentLang?.name)
    }

    override suspend fun getCurrentLang(): CensusLang? {
        return preferences.loadString(CURRENT_CENSUS_LANG)?.let {
            CensusLang.valueOf(it)
        }
    }

    companion object {
        const val PREFERRED_CHARACTER_ID = "preferredCharacterId"
        const val PREFERRED_OUTFIT_ID = "preferredOutfitId"
        const val PREFERRED_NAMESPACE = "preferredNamespace"
        const val PREFERRED_CENSUS_LANG = "preferredCensusLang"
        const val CURRENT_NAMESPACE = "currentNamespace"
        const val CURRENT_CENSUS_LANG = "currentCensusLang"
    }
}
