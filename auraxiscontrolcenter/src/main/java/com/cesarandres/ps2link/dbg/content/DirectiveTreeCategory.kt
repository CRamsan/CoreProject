package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class DirectiveTreeCategory : Comparable<DirectiveTreeCategory> {

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    @Expose
    var name: Name_Multi? = null
    /**
     * @return The directiveTreeCategoryId
     */
    /**
     * @param directiveTreeCategoryId The directive_tree_category_id
     */
    @SerializedName("directive_tree_category_id")
    @Expose
    var directiveTreeCategoryId: String? = null

    var characterDirectiveTreeList: ArrayList<CharacterDirectiveTree>? = null
        private set

    var maxValue: Int = 0
        private set
    var currentValue: Int = 0
        private set

    fun registerCharacterDirectiveTreeList(
        characterDirectiveTree: CharacterDirectiveTree
    ) {
        if (this.characterDirectiveTreeList == null) {
            this.characterDirectiveTreeList = ArrayList()
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
}
