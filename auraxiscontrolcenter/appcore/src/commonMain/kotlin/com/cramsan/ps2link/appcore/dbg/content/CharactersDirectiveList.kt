package com.cramsan.ps2link.appcore.dbg.content

import kotlinx.serialization.SerialName

data class CharactersDirectiveList (

    /**
     * @return The characterId
     */
    /**
     * @param characterId The character_id
     */
    @SerialName("character_id")
    var characterId: String? = null,
    /**
     * @return The completionTime
     */
    /**
     * @param completionTime The completion_time
     */
    @SerialName("completion_time")
    var completionTime: String? = null,
    /**
     * @return The completionTimeDate
     */
    /**
     * @param completionTimeDate The completion_time_date
     */
    @SerialName("completion_time_date")
    var completionTimeDate: String? = null,
    /**
     * @return The directiveId
     */
    /**
     * @param directiveId The directive_id
     */
    @SerialName("directive_id")
    var directiveId: String? = null,
    /**
     * @return The directiveIdJoinDirective
     */
    /**
     * @param directiveIdJoinDirective The directive_id_join_directive
     */
    @SerialName("directive_id_join_directive")
    var directiveIdJoinDirective: Directive? = null,
    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerialName("directive_tree_id")
    var directiveTreeId: String? = null,
)