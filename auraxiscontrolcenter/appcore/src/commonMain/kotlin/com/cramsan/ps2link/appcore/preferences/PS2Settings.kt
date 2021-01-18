package com.cramsan.ps2link.appcore.preferences

import com.cramsan.ps2link.appcore.dbg.CensusLang
import com.cramsan.ps2link.appcore.dbg.Namespace

/**
 * @Author cramsan
 * @created 1/15/2021
 *
 * Save some preferences that are used across the app. This class's interface provides a simple way
 * to store and retrieve the [Namespace] and [CensusLang].
 * The currently supported use cases are the "current" preferences and the "preferred" preferences.
 */
interface PS2Settings {

    suspend fun updatePreferredNamespace(namespace: Namespace?)

    suspend fun getPreferredNamespace(): Namespace?

    suspend fun updatePreferredLang(currentLang: CensusLang?)

    suspend fun getPreferredLang(): CensusLang?

    suspend fun updateCurrentNamespace(namespace: Namespace?)

    suspend fun getCurrentNamespace(): Namespace?

    suspend fun updateCurrentLang(currentLang: CensusLang?)

    suspend fun getCurrentLang(): CensusLang?
}
