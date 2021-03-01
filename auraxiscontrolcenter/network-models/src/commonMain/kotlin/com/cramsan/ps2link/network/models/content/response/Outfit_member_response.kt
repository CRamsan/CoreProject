package com.cramsan.ps2link.network.models.content.response

import com.cramsan.ps2link.network.models.content.Member
import com.cramsan.ps2link.network.models.content.Outfit
import kotlinx.serialization.Serializable

@Serializable
data class Outfit_member_response(
    var outfit_list: List<Outfit>? = null,
    var outfit_member_list: List<Member>? = null,
)
