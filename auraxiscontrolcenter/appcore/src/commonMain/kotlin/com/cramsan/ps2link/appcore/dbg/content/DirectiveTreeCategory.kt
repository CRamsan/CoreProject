package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi
import kotlinx.serialization.SerialName

data class DirectiveTreeCategory(

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    var name: Name_Multi? = null,
    /**
     * @return The directiveTreeCategoryId
     */
    /**
     * @param directiveTreeCategoryId The directive_tree_category_id
     */
    @SerialName("directive_tree_category_id")
    var directiveTreeCategoryId: String? = null,

    var characterDirectiveTreeList: List<CharacterDirectiveTree>? = null,

    var maxValue: Int = 0,

    var currentValue: Int = 0,

    /*
    fun registerCharacterDirectiveTreeList(
        characterDirectiveTree: CharacterDirectiveTree
    ) {
        if (this.characterDirectiveTreeList == null) {
            this.characterDirectiveTreeList = List()
        }
        this.characterDirectiveTreeList!!.add(characterDirectiveTree)
    }

    fun generateValues() {
        for (tree in characterDirectiveTreeList!!) {
            this.maxValue += 145
            when (Integer.parseInt(tree.current_directive_tier_id!!)) {
                0 -> tree.current_level_value = 0
                1 -> tree.current_level_value = 5
                2 -> tree.current_level_value = 15
                3 -> tree.current_level_value = 45
                4 -> tree.current_level_value = 145
                else -> {
                }
            }
            this.currentValue += tree.current_level_value
        }
    }

    override fun compareTo(another: DirectiveTreeCategory): Int {
        return this.name!!.en!!.compareTo(
            another.name!!.en!!,
            ignoreCase = true
        )
    }
     */
)
