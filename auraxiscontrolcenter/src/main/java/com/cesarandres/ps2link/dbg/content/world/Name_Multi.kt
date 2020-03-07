package com.cesarandres.ps2link.dbg.content.world

import com.cesarandres.ps2link.dbg.DBGCensus

open class Name_Multi {
    var de: String? = null
    var en: String? = null
    var es: String? = null
    var fr: String? = null
    var it: String? = null
    var tr: String? = null

    fun localizedName(currentLang: DBGCensus.CensusLang): String? {
        return when (currentLang) {
            DBGCensus.CensusLang.DE -> this.de
            DBGCensus.CensusLang.ES -> this.es
            DBGCensus.CensusLang.FR -> this.fr
            DBGCensus.CensusLang.IT -> this.it
            DBGCensus.CensusLang.TR -> this.tr
            DBGCensus.CensusLang.EN -> this.en
        }
    }
}
