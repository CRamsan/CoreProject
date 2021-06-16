package com.cramsan.ps2link.network.models.content

import kotlinx.serialization.SerialName

data class CharactersDirectiveList(

    /**
     * @return The characterId
     */
    /**
     * @param characterId The character_id
     */
    @SerialName("character_id")
    val characterId: String? = null,
    /**
     * @return The completionTime
     */
    /**
     * @param completionTime The completion_time
     */
    @SerialName("completion_time")
    val completionTime: String? = null,
    /**
     * @return The completionTimeDate
     */
    /**
     * @param completionTimeDate The completion_time_date
     */
    @SerialName("completion_time_date")
    val completionTimeDate: String? = null,
    /**
     * @return The directiveId
     */
    /**
     * @param directiveId The directive_id
     */
    @SerialName("directive_id")
    val directiveId: String? = null,
    /**
     * @return The directiveIdJoinDirective
     */
    /**
     * @param directiveIdJoinDirective The directive_id_join_directive
     */
    @SerialName("directive_id_join_directive")
    val directiveIdJoinDirective: Directive?,
    /**
     * @return The directiveTreeId
     */
    /**
     * @param directiveTreeId The directive_tree_id
     */
    @SerialName("directive_tree_id")
    val directiveTreeId: String? = null,
)
