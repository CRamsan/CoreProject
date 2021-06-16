package com.cramsan.ps2link.network.models.content

data class CharacterDirective(

    val character_id: String? = null,
    val completion_time: String? = null,
    val directive_id: String? = null,
    val directive_tree_id: String? = null,
    val directive_id_join_directive: Directive? = null,
    val directiveObjective: CharacterDirectiveObjective? = null,
    val objective: Objective? = null,
)
