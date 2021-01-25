package com.cramsan.ps2link.appcore.models

import com.cramsan.ps2link.appcore.dbg.LoginStatus
import com.cramsan.ps2link.appcore.dbg.Namespace

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
