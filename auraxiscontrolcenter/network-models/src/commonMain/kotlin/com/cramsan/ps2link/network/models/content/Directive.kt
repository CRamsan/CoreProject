package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.SerialName

data class Directive(

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    val name: Name_Multi? = null,
    /**
     * @return The description
     */
    /**
     * @param description The description
     */
    val description: Description? = null,
    /**
     * @return The directiveId
     */
    /**
     * @param directiveId The directive_id
     */
    @SerialName("directive_id")
    val directiveId: String? = null,
    /**
     * @return The directiveTierId
     */
    /**
     * @param directiveTierId The directive_tier_id
     */
    @SerialName("directive_tier_id")
    val directiveTierId: String? = null,
    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerialName("directive_tree_id")
    val directiveTreeId: String? = null,
    /**
     * @return The imageId
     */
    /**
     * @param imageId The image_id
     */
    @SerialName("image_id")
    val imageId: String? = null,
    /**
     * @return The imagePath
     */
    /**
     * @param imagePath The image_path
     */
    @SerialName("image_path")
    val imagePath: String? = null,
    /**
     * @return The imageSetId
     */
    /**
     * @param imageSetId The image_set_id
     */
    @SerialName("image_set_id")
    val imageSetId: String? = null,
    /**
     * @return The objectiveSetId
     */
    /**
     * @param objectiveSetId The objective_set_id
     */
    @SerialName("objective_set_id")
    val objectiveSetId: String? = null,

    val directive: CharacterDirective? = null,
)
