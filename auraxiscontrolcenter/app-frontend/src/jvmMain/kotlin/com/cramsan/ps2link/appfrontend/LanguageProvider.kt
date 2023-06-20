package com.cramsan.ps2link.appfrontend

import com.cramsan.ps2link.core.models.CensusLang
import java.util.Locale

/**
 *
 */
actual class LanguageProvider {

    /**
     *
     */
    actual fun getCurrentLang(): CensusLang {
        return CensusLang.fromString(getLang())
    }
}

/**
 *
 */
fun getLang(): String = Locale.getDefault().displayLanguage
