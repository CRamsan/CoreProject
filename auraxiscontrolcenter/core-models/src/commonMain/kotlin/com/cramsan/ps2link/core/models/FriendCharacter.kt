package com.cramsan.ps2link.core.models

/**
 * @Author cramsan
 * @created 1/23/2021
 */
data class FriendCharacter(
    val characterId: String,
    val characterName: String?,
    val namespace: Namespace,
    val loginStatus: LoginStatus,
)
