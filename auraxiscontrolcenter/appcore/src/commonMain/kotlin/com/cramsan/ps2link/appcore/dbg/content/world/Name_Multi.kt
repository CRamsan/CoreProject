package com.cramsan.ps2link.appcore.dbg.content.world

import com.cramsan.ps2link.appcore.dbg.CensusLang

open class Name_Multi {
    var de: String? = null
    var en: String? = null
    var es: String? = null
    var fr: String? = null
    var it: String? = null
    var tr: String? = null

    fun localizedName(currentLang: CensusLang): String? {
        return when (currentLang) {
            CensusLang.DE -> this.de
            CensusLang.ES -> this.es
            CensusLang.FR -> this.fr
            CensusLang.IT -> this.it
            CensusLang.TR -> this.tr
            CensusLang.EN -> this.en
        }
    }
}
