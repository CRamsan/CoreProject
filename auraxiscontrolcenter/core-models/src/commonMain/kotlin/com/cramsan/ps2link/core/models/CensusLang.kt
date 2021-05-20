package com.cramsan.ps2link.core.models

enum class CensusLang {
    EN, DE, ES, FR, IT, TR;

    companion object {

        private val enumMapping: Map<String, CensusLang> by lazy {
            values().associateBy { it.name }
        }

        fun fromString(code: String?): CensusLang {
            return if (code == null) {
                EN
            } else {
                enumMapping.getOrElse(code) { EN }
            }
        }
    }
}
