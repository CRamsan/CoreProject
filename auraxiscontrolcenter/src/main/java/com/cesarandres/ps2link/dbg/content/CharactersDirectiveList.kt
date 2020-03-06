package com.cesarandres.ps2link.dbg.content

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CharactersDirectiveList {

    /**
     * @return The characterId
     */
    /**
     * @param characterId The character_id
     */
    @SerializedName("character_id")
    @Expose
    var characterId: String? = null
    /**
     * @return The completionTime
     */
    /**
     * @param completionTime The completion_time
     */
    @SerializedName("completion_time")
    @Expose
    var completionTime: String? = null
    /**
     * @return The completionTimeDate
     */
    /**
     * @param completionTimeDate The completion_time_date
     */
    @SerializedName("completion_time_date")
    @Expose
    var completionTimeDate: String? = null
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
     * @return The directiveIdJoinDirective
     */
    /**
     * @param directiveIdJoinDirective The directive_id_join_directive
     */
    @SerializedName("directive_id_join_directive")
    @Expose
    var directiveIdJoinDirective: Directive? = null
    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerializedName("directive_tree_id")
    @Expose
    var directiveTreeId: String? = null

}
