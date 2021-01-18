package com.cramsan.ps2link.appcore.dbg

enum class Faction(val code: String) {
    VS("1"),
    NC("2"),
    TR("3"),
    NS("4"),
    UNKNOWN(""), ;

    companion object {
        fun fromString(code: String?): Faction {
            return when (code) {
                VS.code -> VS
                NC.code -> NC
                TR.code -> TR
                else -> UNKNOWN
            }
        }
    }
}
