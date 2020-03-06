package com.cesarandres.ps2link.dbg.content.world

import com.cesarandres.ps2link.dbg.DBGCensus

open class Name_Multi {
    var de: String? = null
    var en: String? = null
    var es: String? = null
    var fr: String? = null
    var it: String? = null
    var tr: String? = null

    val localizedName: String?
        get() {
            when (DBGCensus.currentLang) {
                DBGCensus.CensusLang.DE -> return this.de
                DBGCensus.CensusLang.ES -> return this.es
                DBGCensus.CensusLang.FR -> return this.fr
                DBGCensus.CensusLang.IT -> return this.it
                DBGCensus.CensusLang.TR -> return this.tr
                DBGCensus.CensusLang.EN -> return this.en
                else -> return this.en
            }
        }
}
