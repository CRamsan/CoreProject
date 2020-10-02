package com.cesarandres.ps2link.dbg.content

class CharacterDirectiveTree : Comparable<CharacterDirectiveTree> {

    var directive_tree_id_join_directive_tree: DirectiveTree? = null
    var character_id: String? = null
    var completion_time_date: String? = null
    var completion_time: String? = null
    var current_directive_tier_id: String? = null
    var current_level: String? = null
        private set
    var current_level_value: Int = 0
    var directive_tree_id: String? = null
    var directive_tier: DirectiveTier? = null

    fun setCurrrent_level(current_level: String) {
        this.current_level = current_level
    }

    override fun compareTo(another: CharacterDirectiveTree): Int {
        this.directive_tree_id_join_directive_tree!!.name!!.en!!.compareTo(another.directive_tree_id_join_directive_tree!!.name!!.en!!)
        return 1
    }
}
