package com.cramsan.ps2link.appcore.preferences

import com.cramsan.framework.preferences.Preferences
import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace

class PS2SettingsImpl(
    val preferences: Preferences,
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

    override suspend fun updatePreferredProfileNamespace(namespace: Namespace?) {
        preferences.saveString(PREFERRED_CHARACTER_NAMESPACE, namespace?.name)
    }

    override suspend fun getPreferredProfileNamespace(): Namespace? {
        return preferences.loadString(PREFERRED_CHARACTER_NAMESPACE)?.let { Namespace.valueOf(it) }
    }

    override suspend fun updatePreferredOutfitNamespace(namespace: Namespace?) {
        preferences.saveString(PREFERRED_OUTFIT_NAMESPACE, namespace?.name)
    }

    override suspend fun getPreferredOutfitNamespace(): Namespace? {
        return preferences.loadString(PREFERRED_OUTFIT_NAMESPACE)?.let { Namespace.valueOf(it) }
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
            CensusLang.fromString(it)
        }
    }

    companion object {
        const val PREFERRED_CHARACTER_ID = "preferredCharacterId"
        const val PREFERRED_OUTFIT_ID = "preferredOutfitId"
        const val PREFERRED_CHARACTER_NAMESPACE = "preferredCharacterNamespace"
        const val PREFERRED_OUTFIT_NAMESPACE = "preferredOutfitNamespace"
        const val CURRENT_NAMESPACE = "currentNamespace"
        const val CURRENT_CENSUS_LANG = "currentCensusLang"
    }
}
