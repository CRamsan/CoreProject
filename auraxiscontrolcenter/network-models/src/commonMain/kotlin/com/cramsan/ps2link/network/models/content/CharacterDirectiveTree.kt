package com.cramsan.ps2link.network.models.content

data class CharacterDirectiveTree(
    val directive_tree_id_join_directive_tree: DirectiveTree?,
    val character_id: String? = null,
    val completion_time_date: String? = null,
    val completion_time: String? = null,
    val current_directive_tier_id: String? = null,
    val current_level: String? = null,
    val current_level_value: Int?,
    val directive_tree_id: String? = null,
    val directive_tier: DirectiveTier?,

    /*
    fun setCurrrent_level(current_level: String) {
        this.current_level = current_level
    }

    override fun compareTo(another: CharacterDirectiveTree): Int {
        this.directive_tree_id_join_directive_tree!!.name!!.en!!.compareTo(another.directive_tree_id_join_directive_tree!!.name!!.en!!)
        return 1
    }
     */
)
