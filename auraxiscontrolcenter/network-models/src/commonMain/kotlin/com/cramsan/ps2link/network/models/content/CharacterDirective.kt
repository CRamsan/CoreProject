package com.cramsan.ps2link.network.models.content

data class CharacterDirective(

    var character_id: String? = null,
    var completion_time: String? = null,
    var directive_id: String? = null,
    var directive_tree_id: String? = null,
    var directive_id_join_directive: Directive? = null,
    var directiveObjective: CharacterDirectiveObjective? = null,
    var objective: Objective? = null,
)
