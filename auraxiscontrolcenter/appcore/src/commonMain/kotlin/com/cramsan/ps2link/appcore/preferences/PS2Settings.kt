package com.cramsan.ps2link.appcore.preferences

import com.cramsan.ps2link.core.models.CensusLang
import com.cramsan.ps2link.core.models.Namespace

/**
 * @Author cramsan
 * @created 1/15/2021
 *
 * Save some preferences that are used across the app. This class's interface provides a simple way
 * to store and retrieve the [Namespace] and [CensusLang].
 * The currently supported use cases are the "current" preferences and the "preferred" preferences.
 */
interface PS2Settings {

    suspend fun updatePreferredCharacterId(characterId: String?)

    suspend fun getPreferredCharacterId(): String?

    suspend fun updatePreferredOutfitId(outfitId: String?)

    suspend fun getPreferredOutfitId(): String?

    suspend fun updatePreferredProfileNamespace(namespace: Namespace?)

    suspend fun getPreferredProfileNamespace(): Namespace?

    suspend fun updatePreferredOutfitNamespace(namespace: Namespace?)

    suspend fun getPreferredOutfitNamespace(): Namespace?

    suspend fun updateCurrentNamespace(namespace: Namespace?)

    suspend fun getCurrentNamespace(): Namespace?

    suspend fun updateCurrentLang(currentLang: CensusLang?)

    suspend fun getCurrentLang(): CensusLang?
}
