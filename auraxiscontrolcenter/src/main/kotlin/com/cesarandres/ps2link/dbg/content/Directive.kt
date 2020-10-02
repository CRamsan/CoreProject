package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Directive {

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    @Expose
    var name: Name_Multi? = null
    /**
     * @return The description
     */
    /**
     * @param description The description
     */
    @Expose
    var description: Description? = null
    /**
     * @return The directiveId
     */
    /**
     * @param directiveId The directive_id
     */
    @SerializedName("directive_id")
    @Expose
    var directiveId: String? = null
    /**
     * @return The directiveTierId
     */
    /**
     * @param directiveTierId The directive_tier_id
     */
    @SerializedName("directive_tier_id")
    @Expose
    var directiveTierId: String? = null
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
    /**
     * @return The objectiveSetId
     */
    /**
     * @param objectiveSetId The objective_set_id
     */
    @SerializedName("objective_set_id")
    @Expose
    var objectiveSetId: String? = null

    var directive: CharacterDirective? = null
}
