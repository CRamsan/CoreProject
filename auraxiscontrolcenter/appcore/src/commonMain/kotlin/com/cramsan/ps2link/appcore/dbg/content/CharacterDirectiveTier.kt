package com.cramsan.ps2link.appcore.dbg.content

data class CharacterDirectiveTier (

    var charactersDirective: List<CharacterDirective>? = null,
    var character_id: String? = null,
    var completion_time: String? = null,
    var directive_tier_id: String? = null,
    var directive_tree_id: String? = null,
    var directive_tier_id_join_directive_tier: DirectiveTier? = null,

    /*
    fun registerDirective(newDirective: CharacterDirective) {
        if (charactersDirective == null) {
            charactersDirective = List()
        }
        charactersDirective!!.add(newDirective)
    }
    */
)