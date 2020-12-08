package com.cramsan.ps2link.appcore.dbg.content

import com.cramsan.ps2link.appcore.dbg.content.world.Name_Multi
import kotlinx.serialization.SerialName

data class Directive (

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    var name: Name_Multi? = null,
    /**
     * @return The description
     */
    /**
     * @param description The description
     */
    var description: Description? = null,
    /**
     * @return The directiveId
     */
    /**
     * @param directiveId The directive_id
     */
    @SerialName("directive_id")
    var directiveId: String? = null,
    /**
     * @return The directiveTierId
     */
    /**
     * @param directiveTierId The directive_tier_id
     */
    @SerialName("directive_tier_id")
    var directiveTierId: String? = null,
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
    /**
     * @return The objectiveSetId
     */
    /**
     * @param objectiveSetId The objective_set_id
     */
    @SerialName("objective_set_id")
    var objectiveSetId: String? = null,

    var directive: CharacterDirective? = null,
)