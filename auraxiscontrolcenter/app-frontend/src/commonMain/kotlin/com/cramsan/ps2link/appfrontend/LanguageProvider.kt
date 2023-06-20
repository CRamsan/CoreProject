package com.cramsan.ps2link.appfrontend

import com.cramsan.ps2link.core.models.CensusLang

/**
 *
 */
expect class LanguageProvider {

    /**
     *
     */
    fun getCurrentLang(): CensusLang
}
