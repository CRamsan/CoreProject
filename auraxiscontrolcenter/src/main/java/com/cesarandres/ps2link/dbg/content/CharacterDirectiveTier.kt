package com.cesarandres.ps2link.dbg.content

import java.util.ArrayList


class CharacterDirectiveTier {


    var charactersDirective: ArrayList<CharacterDirective>? = null
    var character_id: String? = null
    var completion_time: String? = null
    var directive_tier_id: String? = null
    var directive_tree_id: String? = null
    var directive_tier_id_join_directive_tier: DirectiveTier? = null

    fun registerDirective(newDirective: CharacterDirective) {
        if (charactersDirective == null) {
            charactersDirective = ArrayList()
        }
        charactersDirective!!.add(newDirective)
    }

}
