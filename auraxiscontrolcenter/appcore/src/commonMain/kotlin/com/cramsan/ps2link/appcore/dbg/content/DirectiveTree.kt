package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi
import kotlinx.serialization.SerialName


data class DirectiveTree(

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

    /**
     * @return The directiveTreeCategoryIdJoinDirectiveTreeCategory
     */
    /**
     * @param directiveTreeCategoryIdJoinDirectiveTreeCategory The directive_tree_category_id_join_directive_tree_category
     */
    @SerialName("directive_tree_category_id_join_directive_tree_category")
    var directiveTreeCategoryIdJoinDirectiveTreeCategory: DirectiveTreeCategory? = null,

    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerialName("directive_tree_id")
    var directiveTreeId: String? = null,

    /**
     * @return The imageId
     */
    /**
     * @param imageId The image_id
     */
    @SerialName("image_id")
    var imageId: String? = null,

    /**
     * @return The imagePath
     */
    /**
     * @param imagePath The image_path
     */
    @SerialName("image_path")
    var imagePath: String? = null,

    /**
     * @return The imageSetId
     */
    /**
     * @param imageSetId The image_set_id
     */
    @SerialName("image_set_id")
    var imageSetId: String? = null,
)