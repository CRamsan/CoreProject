package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DirectiveTree {

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

    /**
     * @return The directiveTreeCategoryIdJoinDirectiveTreeCategory
     */
    /**
     * @param directiveTreeCategoryIdJoinDirectiveTreeCategory The directive_tree_category_id_join_directive_tree_category
     */
    @SerializedName("directive_tree_category_id_join_directive_tree_category")
    @Expose
    var directiveTreeCategoryIdJoinDirectiveTreeCategory: DirectiveTreeCategory? = null

    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerializedName("directive_tree_id")
    @Expose
    var directiveTreeId: String? = null

    /**
     * @return The imageId
     */
    /**
     * @param imageId The image_id
     */
    @SerializedName("image_id")
    @Expose
    var imageId: String? = null

    /**
     * @return The imagePath
     */
    /**
     * @param imagePath The image_path
     */
    @SerializedName("image_path")
    @Expose
    var imagePath: String? = null

    /**
     * @return The imageSetId
     */
    /**
     * @param imageSetId The image_set_id
     */
    @SerializedName("image_set_id")
    @Expose
    var imageSetId: String? = null
}
