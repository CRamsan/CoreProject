package com.cramsan.ps2link.network.models.content

data class CharacterDirectiveTier(

    val charactersDirective: List<CharacterDirective>?,
    val character_id: String? = null,
    val completion_time: String? = null,
    val directive_tier_id: String? = null,
    val directive_tree_id: String? = null,
    val directive_tier_id_join_directive_tier: DirectiveTier?,

    /*
    fun registerDirective(newDirective: CharacterDirective) {
        if (charactersDirective == null) {
            charactersDirective = List()
        }
        charactersDirective!!.add(newDirective)
    }
    */
)
