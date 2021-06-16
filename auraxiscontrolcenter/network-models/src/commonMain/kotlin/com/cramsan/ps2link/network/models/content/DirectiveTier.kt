package com.cramsan.ps2link.network.models.content

import com.cramsan.ps2link.network.models.content.world.Name_Multi
import kotlinx.serialization.SerialName

data class DirectiveTier(

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    val name: Name_Multi? = null,
    /**
     * @return The completionCount
     */
    /**
     * @param completionCount The completion_count
     */
    @SerialName("completion_count")
    val completionCount: String? = null,
    /**
     * @return The directivePoints
     */
    /**
     * @param directivePoints The directive_points
     */
    @SerialName("directive_points")
    val directivePoints: String? = null,
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
     * @return The rewardSetId
     */
    /**
     * @param rewardSetId The reward_set_id
     */
    @SerialName("reward_set_id")
    val rewardSetId: String? = null,

    val directives: List<Directive>? = null,

            /*
    fun registerDirective(directive: Directive) {
        if (this.directives == null) {
            this.directives = List()
        }
        this.directives!!.add(directive)
    }
             */
)
