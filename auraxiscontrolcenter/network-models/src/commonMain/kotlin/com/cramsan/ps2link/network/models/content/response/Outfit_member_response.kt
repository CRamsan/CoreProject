package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.Member
import kotlinx.serialization.Serializable

@Serializable
data class Outfit_member_response(
    val outfit_member_list: List<Member>? = null,
)
