package com.cesarandres.ps2link.dbg.content

import com.cesarandres.ps2link.dbg.content.world.Name_Multi
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList


class DirectiveTier {

    /**
     * @return The name
     */
    /**
     * @param name The name
     */
    @Expose
    var name: Name_Multi? = null
    /**
     * @return The completionCount
     */
    /**
     * @param completionCount The completion_count
     */
    @SerializedName("completion_count")
    @Expose
    var completionCount: String? = null
    /**
     * @return The directivePoints
     */
    /**
     * @param directivePoints The directive_points
     */
    @SerializedName("directive_points")
    @Expose
    var directivePoints: String? = null
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
     * @return The rewardSetId
     */
    /**
     * @param rewardSetId The reward_set_id
     */
    @SerializedName("reward_set_id")
    @Expose
    var rewardSetId: String? = null

    var directives: ArrayList<Directive>? = null
        private set

    fun registerDirective(directive: Directive) {
        if (this.directives == null) {
            this.directives = ArrayList()
        }
        this.directives!!.add(directive)
    }
}
